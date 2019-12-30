package com.orderfresh.midtier.restaurant.handler;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.reactivestreams.client.gridfs.GridFSBucket;
import com.orderfresh.midtier.restaurant.flux.DownloadStreamToFluxFactory;
import com.orderfresh.midtier.restaurant.flux.FluxAsyncStreamConverter;
import com.orderfresh.midtier.restaurant.model.Identifier;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.Part;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class ImageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageHandler.class);
    //Test123
    private final GridFSBucket gridFSBucket = null;
    private final DownloadStreamToFluxFactory downloadStreamToFluxFactory = new DownloadStreamToFluxFactory();

    //Test123
    /*@Autowired
    public ImageHandler(GridFSBucket gridFSBucket) {
        this.gridFSBucket = gridFSBucket;
    }*/

    public Flux<String> saveImage(Flux<Part> multipartFile) {
        return multipartFile
                .flatMap(part -> Mono.from(gridFSBucket.uploadFromStream(part.name(),
                                                                         FluxAsyncStreamConverter.convert(part.content()))))
                .map(ObjectId::toHexString);
    }

    public Flux<byte[]> fetchImage(String imageId) {
        return downloadStreamToFluxFactory
                .convert(gridFSBucket.openDownloadStream(getId(imageId)));
    }

    public Mono<Boolean> removeImage(String imageId) {
        return Mono.from(gridFSBucket.delete(getId(imageId)))
                   .map(Objects::nonNull)
                   .onErrorReturn(false);
    }

    public Flux<Identifier> findAllNames() {
        return Flux.from(gridFSBucket.find())
                   .map(GridFSFile::getId)
                   .map(BsonValue::asObjectId)
                   .map(BsonObjectId::getValue)
                   .map(ObjectId::toHexString)
                   .map(Identifier::new);

    }

    private ObjectId getId(String imageId) {
        return new ObjectId(imageId);
    }
}