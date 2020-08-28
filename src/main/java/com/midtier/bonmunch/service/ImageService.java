package com.midtier.bonmunch.service;

import com.midtier.bonmunch.model.ImageResult;
import com.midtier.bonmunch.web.model.Image;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.config.S3ClientConfigurarionProperties;
import com.midtier.bonmunch.exception.S3FailedException;
import com.midtier.bonmunch.factory.ImageDomainFactory;
import com.midtier.bonmunch.repository.dao.ImageRepository;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkResponse;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageService {

    @Autowired
    S3AsyncClient s3client;

    @Autowired
    S3ClientConfigurarionProperties s3config;

    @Autowired
    AwsCredentialsProvider awsCredentialsProvider;


    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageDomainFactory imageDomainFactory;

    @Value("${page.image.min.size}")
    private String pageImageSize;


    public Mono<Image> getAllGalleryImagesInfo(int pageNumber, ImageType imageType, UUID companyId) {

        return imageRepository.findByImageTypeAndCompanyId(imageType, companyId,
                                               PageRequest.of(pageNumber,Integer.valueOf(pageImageSize)))
                      .subscribeOn(Schedulers.elastic())
                      .publishOn(Schedulers.parallel())
                      .map( image -> imageDomainFactory.getUpdatedImage(image, imageType, companyId) )
                      .collectList()
                      .map(imagesList -> imageDomainFactory.getGalleryImages(imagesList) );

    }

    public Mono<Flux<ByteBuffer>> downloadImage(String filekey) {

        GetObjectRequest request = GetObjectRequest.builder()
                                                   .bucket(s3config.getBucket())
                                                   .key(filekey)
                                                   .build();

        return Mono.fromFuture(s3client.getObject(request,new FluxResponseProvider()))
                   .map( (response) -> {
                       checkResult(response.sdkResponse);
                       String filename = getMetadataItem(response.sdkResponse,"filename",filekey);

                       log.info("[I65] filename={}, length={}",filename, response.sdkResponse.contentLength() );

                       return response.flux;
                   });
    }

    /**
     *  Standard file upload.
     */
    public Mono<ImageResult>    singleUpload(Flux<ByteBuffer> body, long length, MediaType mediaType) {

        if (length < 0) {
            throw new S3FailedException(HttpStatus.BAD_REQUEST.value(), Optional.of("required header missing: Content-Length"));
        }

        String fileKey = UUID.randomUUID().toString();
        Map<String, String> metadata = new HashMap<String, String>();

        if (mediaType == null) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        log.info("[I95] uploadHandler: mediaType{}, length={}", mediaType, length);
        CompletableFuture<PutObjectResponse> future = s3client
                .putObject(PutObjectRequest.builder()
                                           .bucket(s3config.getBucket())
                                           .contentLength(length)
                                           .key(fileKey.toString())
                                           .contentType(mediaType.toString())
                                           .metadata(metadata)
                                           .build(),
                           AsyncRequestBody.fromPublisher(body));

        return Mono.fromFuture(future)
                   .map((response) -> {
                       checkResult(response);
                       return new ImageResult(HttpStatus.CREATED, new String[] {fileKey});
                   });
    }

    public Mono<ImageResult> multipartUpload(Flux<Part> parts, ImageType imageType, String imageName, ApiPrincipal apiPrincipal ) {

        return parts
                .ofType(FilePart.class) // We'll ignore other data for now
                .flatMap((part) -> saveFile(imageType, s3config.getBucket(), part, apiPrincipal))
                // save image ids to app database
                .flatMap(key -> imageRepository.save(
                        new com.midtier.bonmunch.model.Image(
                                UUID.fromString(key),
                                apiPrincipal.getCompanyId(),
                                imageType,
                                imageName)))
                .map(result -> result.getId())
                .publishOn(Schedulers.elastic())
                .subscribeOn(Schedulers.parallel())
                .map(result -> result.toString())
                .collect(Collectors.toList())
                .map((keys) -> new ImageResult(HttpStatus.CREATED, keys));


    }

    /**
     * Save file using a multipart upload. This method does not require any temporary
     * storage at the REST service
     * @param bucket Bucket name
     * @param part Uploaded file
     * @return
     */
    protected Mono<String> saveFile(ImageType imageType, String bucket, FilePart part, ApiPrincipal apiPrincipal) {

        // Generate a filekey for this upload
        String imageId = UUID.randomUUID().toString();
        String filekey = apiPrincipal.getCompanyId()+"/"+imageType.toString()+"/"+imageId;

        log.info("[I137] saveFile: filekey={}, filename={}", filekey, part.filename());

        // Gather metadata
        Map<String, String> metadata = new HashMap<String, String>();
        String filename = part.filename();
        if ( filename == null ) {
            filename = filekey;
        }

        metadata.put("filename", filename);

        MediaType mt = part.headers().getContentType();
        if ( mt == null ) {
            mt = MediaType.APPLICATION_OCTET_STREAM;
        }

        // Create multipart upload request
        CompletableFuture<CreateMultipartUploadResponse> uploadRequest = s3client
                .createMultipartUpload(CreateMultipartUploadRequest.builder()
                                                                   .contentType(mt.toString())
                                                                   //.key(apiPrincipal.getCompanyId()+"/"+imageType.toString()+"/"+filekey)
                                                                   .key(filekey)
                                                                   .acl("public-read")
                                                                   .metadata(metadata)
                                                                   .bucket(bucket)
                                                                   .build());

        // This variable will hold the upload state that we must keep
        // around until all uploads complete
        final UploadState uploadState = new UploadState(bucket,filekey, imageId);

        return Mono
                .fromFuture(uploadRequest)
                .flatMapMany((response) -> {
                    checkResult(response);
                    uploadState.uploadId = response.uploadId();
                    log.info("[I183] uploadId={}", response.uploadId());
                    return part.content();
                })
                .bufferUntil((buffer) -> {
                    uploadState.buffered += buffer.readableByteCount();
                    if ( uploadState.buffered >= s3config.getMultipartMinPartSize() ) {
                        log.info("[I173] bufferUntil: returning true, bufferedBytes={}, partCounter={}, uploadId={}", uploadState.buffered, uploadState.partCounter, uploadState.uploadId);
                        uploadState.buffered = 0;
                        return true;
                    }
                    else {
                        return false;
                    }
                })
                .map((buffers) -> concatBuffers(buffers))
                .flatMap((buffer) -> uploadPart(uploadState,buffer))
                .onBackpressureBuffer()
                .reduce(uploadState,(state,completedPart) -> {
                    log.info("[I188] completed: partNumber={}, etag={}", completedPart.partNumber(), completedPart.eTag());
                    state.completedParts.put(completedPart.partNumber(), completedPart);
                    return state;
                })
                .flatMap((state) -> completeUpload(state))
                .map((response) -> {
                    checkResult(response);
                    return  uploadState.imageId;
                });
    }


    private static ByteBuffer concatBuffers(List<DataBuffer> buffers) {
        log.info("[I198] creating BytBuffer from {} chunks", buffers.size());

        int partSize = 0;
        for( DataBuffer b : buffers) {
            partSize += b.readableByteCount();
        }

        ByteBuffer partData = ByteBuffer.allocate(partSize);
        buffers.forEach((buffer) -> {
            partData.put(buffer.asByteBuffer());
        });

        // Reset read pointer to first byte
        partData.rewind();

        log.info("[I208] partData: size={}", partData.capacity());
        return partData;

    }


    /**
     * Upload a single file part to the requested bucket
     * @param uploadState
     * @param buffer
     * @return
     */
    private Mono<CompletedPart> uploadPart(UploadState uploadState, ByteBuffer buffer) {
        final int partNumber = ++uploadState.partCounter;
        log.info("[I218] uploadPart: partNumber={}, contentLength={}",partNumber, buffer.capacity());

        CompletableFuture<UploadPartResponse> request = s3client.uploadPart(UploadPartRequest.builder()
                                                                                             .bucket(uploadState.bucket)
                                                                                             .key(uploadState.filekey)
                                                                                             .partNumber(partNumber)
                                                                                             .uploadId(uploadState.uploadId)
                                                                                             .contentLength((long) buffer.capacity())
                                                                                             .build(),
                                                                            AsyncRequestBody.fromPublisher(Mono.just(buffer)));

        return Mono
                .fromFuture(request)
                .map((uploadPartResult) -> {
                    checkResult(uploadPartResult);
                    log.info("[I230] uploadPart complete: part={}, etag={}",partNumber,uploadPartResult.eTag());
                    return CompletedPart.builder()
                                        .eTag(uploadPartResult.eTag())
                                        .partNumber(partNumber)
                                        .build();
                });
    }

    private Mono<CompleteMultipartUploadResponse> completeUpload(UploadState state) {
        log.info("[I202] completeUpload: bucket={}, filekey={}, completedParts.size={}", state.bucket, state.filekey, state.completedParts.size());

        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
                                                                           .parts(state.completedParts.values())
                                                                           .build();

        return Mono.fromFuture(s3client.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                                                                                              .bucket(state.bucket)
                                                                                              .uploadId(state.uploadId)
                                                                                              .multipartUpload(multipartUpload)
                                                                                              .key(state.filekey)
                                                                                              .build()));
    }


    public Mono<Void> deleteImage(UUID imageId, ImageType imageType, ApiPrincipal apiPrincipal) {

        String filekey = apiPrincipal.getCompanyId()+"/"+imageType.toString()+"/"+imageId;
        // Create multipart upload request
        CompletableFuture<DeleteObjectResponse> deleteObjectResponse = s3client
                .deleteObject(DeleteObjectRequest.builder()
                                                 .bucket(s3config.getBucket())
                                                 .key(filekey)
                                                 .build());


        return Mono
                .fromFuture(deleteObjectResponse)
                .flatMap((response) -> {
                    return imageRepository.deleteById(imageId);
                });

    }


    /**
     * Lookup a metadata key in a case-insensitive way.
     * @param sdkResponse
     * @param key
     * @param defaultValue
     * @return
     */
    private String getMetadataItem(GetObjectResponse sdkResponse, String key, String defaultValue) {
        for( Map.Entry<String, String> entry : sdkResponse.metadata().entrySet()) {
            if ( entry.getKey().equalsIgnoreCase(key)) {
                return entry.getValue();
            }
        }
        return defaultValue;
    }


    /**
     * check result from an API call.
     * @param result Result from an API call
     */
    private static void checkResult(SdkResponse result) {
        if (result.sdkHttpResponse() == null || !result.sdkHttpResponse().isSuccessful()) {
            throw new S3FailedException(result);
        }
    }


    /**
     * Holds upload state during a multipart upload
     */
    static class UploadState {
        final String bucket;
        final String filekey;
        final String imageId;

        String uploadId;
        int partCounter;
        Map<Integer, CompletedPart> completedParts = new HashMap<>();
        int buffered = 0;

        UploadState(String bucket, String filekey, String imageId) {
            this.bucket = bucket;
            this.filekey = filekey;
            this.imageId = imageId;
        }
    }


    static class FluxResponseProvider implements AsyncResponseTransformer<GetObjectResponse,FluxResponse> {

        private FluxResponse response;

        @Override
        public CompletableFuture<FluxResponse> prepare() {
            response = new FluxResponse();
            return response.cf;
        }

        @Override
        public void onResponse(GetObjectResponse sdkResponse) {
            this.response.sdkResponse = sdkResponse;
        }

        @Override
        public void onStream(SdkPublisher<ByteBuffer> publisher) {
            response.flux = Flux.from(publisher);
            response.cf.complete(response);
        }

        @Override
        public void exceptionOccurred(Throwable error) {
            response.cf.completeExceptionally(error);
        }

    }

    /**
     * Holds the API response and stream
     */
    static class FluxResponse {

        final CompletableFuture<FluxResponse> cf = new CompletableFuture<>();
        GetObjectResponse sdkResponse;
        Flux<ByteBuffer> flux;
    }


}
