package com.midtier.bonmunch.dummy.web.controller;

import com.midtier.bonmunch.dummy.model.MenuItem;
import com.midtier.bonmunch.dummy.service.DummyService;
import com.midtier.bonmunch.model.Customer;
import com.midtier.bonmunch.dummy.model.Message;
import com.midtier.bonmunch.dummy.model.CustomerInfo;
import com.midtier.bonmunch.dummy.model.Order;
import com.midtier.bonmunch.dummy.model.Person;
import com.midtier.bonmunch.dummy.model.Person2;
import com.midtier.bonmunch.dummy.model.Promotion;
import com.midtier.bonmunch.model.Restaurant;
import com.midtier.bonmunch.dummy.model.RestaurantAdminInfo;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.security.model.AuthRequest;
import com.midtier.bonmunch.security.model.AuthResponse;
import com.midtier.bonmunch.service.AwsService;
import com.midtier.bonmunch.service.CompanyService;
import com.midtier.bonmunch.service.ImageService;
import com.midtier.bonmunch.service.RestaurantService;
import com.midtier.bonmunch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

public class DummyController {

    @Autowired
    private UserService userService;

    @Autowired
    private AwsService awsAdapter;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    DummyService dummyService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CompanyService companyService;


    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> user(@AuthenticationPrincipal ApiPrincipal principal) {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }


    @PostMapping(value = "/v1/restaurants/fileUpload",produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> upload(//@RequestPart("file") MultipartFile file) {
                                                @RequestParam(value = "file",required = false) MultipartFile file) {
        URL url = awsAdapter.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType());

        return Mono.just(ResponseEntity.ok(new Message(((URL) url).toString())));
    }

    @PostMapping(value = "/v1/restaurants/fileUpload1")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        return Mono.just(ResponseEntity.ok("123"));
    }


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {

        return "\\\\";
    }


    @PostMapping("/upload")
    Mono<String> upload(@RequestParam Map<String, Part> parts) {
        Part filePart = parts.get("files");
        //filePart.transferTo(new File( "/" + filePart.getFilename().get()));
        return Mono.just("");
    }


    /*@PostMapping(value = "/v1/restaurants/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Message>> upload(//@RequestPart("file") MultipartFile file) {
                                                @RequestParam(value = "file",required = false) MultipartFile file) {
        URL url = awsAdapter.storeObjectInS3(file, file.getOriginalFilename(), file.getContentType());

        return Mono.just(ResponseEntity.ok(new Message(((URL) url).toString())));
    }*/

    /*@GetMapping
    public void getMedicalRecords(@RequestParam String fileName, HttpServletResponse response) throws IOException {
        S3Object s3Object = awsAdapter.fetchObject(fileName);
        InputStream stream = s3Object.getObjectContent();
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        IOUtils.copy(stream, response.getOutputStream());
    }*/




    // commenting
    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar) {
        return userRepository.findByUsername(ar.getUsername()).map((userDetails) -> {
            if (passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }*/


    // commenting
    /*@RequestMapping(value = "/resource/user", method = RequestMethod.GET)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<String>> user() {
        return Mono.just(ResponseEntity.ok("correct user"));
    }*/




    /*@PostMapping(value = "/restaurant/v1/promotions/photos123", consumes = { "multipart/form-data" })
    public Mono<Void> addPhoto123(@RequestPart("file") Flux<FilePart> file){
        //String id = photoService.addPhoto(title, image);
        //return imageHandler.saveImage(file);


        return restaurantService.createImage1(file);

    }
*/



    /*@PostMapping(value = "/restaurant/v1/promotions1",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Promotion> saveImage(@RequestParam("title") String title, @RequestPart("file") Mono<FilePart> part) throws Exception{

        new Binary(BsonBinarySubType.BINARY, file.getBytes());
        return part.flatMap(file -> imageService.saveImage(file));
    }*/


    /*@GetMapping(value = "/restaurant/v1/restaurants/{id}/menu_items")
    public Mono<ResponseEntity<List<MenuItem>>> getMenuItems(
            @Valid @PathVariable String id
    ){

        return restaurantService.getMenuItems()`
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }*/



    /*
        Get all restaurants
     */
    @GetMapping(value = "/restaurant/v1/restaurants")
    public Mono<ResponseEntity<List<Restaurant>>> getRestaurants(
    ){

        return dummyService.getRestaurants()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @GetMapping(value = "/restaurant/v1/customerInfo/{id}")
    public Mono<ResponseEntity<CustomerInfo>> getCustomerInfoEntity(
            @Valid @PathVariable UUID id
    ){

        return dummyService.getCustomerInfo(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }

    @PostMapping(value = "/restaurant/v1/customersInfo")
    public Mono<ResponseEntity<CustomerInfo>> createCustomerInfo(
            @Valid @RequestBody Mono<CustomerInfo> customerInfoMono
    ){

        return dummyService.createCustomerInfo(customerInfoMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));

    }

    @PostMapping(value = "/restaurant/v1/promotions")
    public Mono<ResponseEntity<Promotion>> createPromotion(
            @Valid @RequestBody Mono<Promotion> userMono
    ){

        return dummyService.createPromotion(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    @PostMapping(value = "/restaurant/v1/promotions1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<FilePart> saveImage(@RequestPart("file") Mono<FilePart> part) throws Exception{
        return part;
    }

    @PostMapping("/restaurant/v1/promotions12")
    public Mono<String> process(@PathVariable("test") String test,
                                @RequestPart("files") Flux<FilePart> filePartFlux) {
        return filePartFlux.flatMap(it -> it.transferTo(getPath(it)))
                           .then(Mono.just("OK"));
    }

    private File getPath(FilePart it) {
        Path df = Paths.get("/tmp/" + it.filename());
        return null;
    }


    @PostMapping("/restaurant/v1/promotions/restaurant/{id}")
    public Mono<String> process2(@PathVariable("id") String test,
                                 @RequestPart("file") FilePart filePart) {


        //f//ilePart.
        return Mono.just(new String());
    }


    @PostMapping(value = "/restaurant/v1/promotions/photos", consumes = { "multipart/form-data" })
    public String addPhoto(@RequestPart("file") MultipartFile file){
        //String id = photoService.addPhoto(title, image);
        return "redirect:/photos/";
    }



    @GetMapping(value = "/restaurant/v1/restaurants/{id}/promotions")
    public Mono<ResponseEntity<List<Promotion>>> getPromotions(
            @Valid @PathVariable String id
    ){

        return dummyService.getPromotions(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @PostMapping(value = "/restaurant/v1/persons")
    public Mono<ResponseEntity<Person>> createPersonForEntity(
            @Valid @RequestBody Mono<Person> userMono
    ){

        return dummyService.createPerson(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    @PostMapping(value = "/restaurant/v1/persons/session")
    public Mono<ResponseEntity<Person>> getPersonForEntity(
            @Valid @RequestBody Mono<Person> userMono
    ){

        return dummyService.loginPerson(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(402).body(null));
    }


    @PostMapping(value = "/restaurant/v1/persons2/session")
    public Mono<ResponseEntity<Person2>> getPerson2ForEntity(
            @Valid @RequestBody Mono<Person2> userMono
    ){

        return dummyService.loginPerson2(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(402).body(null));
    }



    @CrossOrigin
    @GetMapping(value = "/restaurant/v1/restaurants/adminInfo")
    public Mono<ResponseEntity<RestaurantAdminInfo>> getRestaurantAdminInfo(
    ){

        return dummyService.getRestaurantAdminInfo()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }




    /*
        Delete the customer/user from session when logout
     */
    @DeleteMapping(value = "/restaurant/v1/users/session")
    public Mono<ResponseEntity> deleteFromEntity(){

        //restaurantService.deleteUser();
        return Mono.just(new ResponseEntity<>(null, HttpStatus.OK));
    }



    /*
        Get restaurant details
     */
    @GetMapping(value = "/v1/customers/getAllRestaurants")
    public Mono<ResponseEntity<List<Restaurant>>> getAllRestaurants(
    ){

        return dummyService.getRestaurants()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }



    /*
        create order for the customer/company
     */
    @PostMapping(value = "/v1/customers/{id}/orders")
    public Mono<ResponseEntity<Order>> createOrdersForEntity(
            @Valid @RequestBody Mono<Order> userMono
    ){

        return dummyService.createOrder(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    // Restaurant Controller

    /*
    @PostMapping(value = "/v1/restaurants/order")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Order1>> createOrder(
            @Valid @RequestBody Mono<Order1> order1Mono,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return userService.createOrder(order1Mono, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }
    */

    @PostMapping(
            value = {"/v1/admin/test"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<AuthResponse>> test(@RequestBody AuthRequest ar) {

        Mono<AuthResponse> result = Mono.just(new AuthResponse());

        return result.map(resp -> new ResponseEntity<>(
                                  resp,
                                  HttpStatus.OK
                          )
        )
                     .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                                   .body(null));
    }



    /*
        Get/download one image by imageId
     */
    @GetMapping(value = "/v1/restaurants/{id}/galleryImages/{imageId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Flux<ByteBuffer>>> getDownloadedImage(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal,
            @Valid @PathVariable UUID id,
            @Valid @PathVariable UUID imageId
    ){

        return
                imageService.downloadImage(id.toString())
                            .map(resp -> {

                                return ResponseEntity.ok()
                                                     //.header(HttpHeaders.CONTENT_TYPE, resp.sdkResponse.contentType())
                                                     //.header(HttpHeaders.CONTENT_LENGTH, Long.toString(response.sdkResponse.contentLength()))
                                                     //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                                                     .body(resp);
                            })
                            .defaultIfEmpty(ResponseEntity.status(400).body(null));

    }



    /*
        Get menuItems w.r.t the restaurant id
     */
    @GetMapping(value = "/restaurant/v1/restaurants/{id}/menu_items")
    public Mono<ResponseEntity<List<MenuItem>>> getMenuItems(
            @Valid @PathVariable String id
    ){

        return dummyService.getMenuItems()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }




    /*
        create order for the restaurant
     */
/*    @PostMapping(value = "/v1/restaurant/orders")
    public Mono<ResponseEntity<Order>> createOrdersForRestaurant(
            @Valid @RequestBody Mono<Order> userMono
    ){

        return restaurantService.createOrder(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

*/

    // Admin Controller



    /*
       Create new Customer in a restaurant
    */
    @PostMapping(value = "/v1/companies/customers1")
    public Mono<ResponseEntity<Customer>> createNewCustomer(
            @Valid @RequestBody Mono<Customer> customerMono
    ){

        return dummyService.createCustomer(customerMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Get customer/company information
     */
    @GetMapping(value = "/v1/companies1/{id}")
    public Mono<ResponseEntity<Customer>> getCustomerInfo(
            @Valid @PathVariable UUID id
    ){

        return dummyService.getCustomer(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }

    /*
        For creating user for entity endpoint (admin user)
     */
/*    @PostMapping(value = "/v1/admin/user",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> createUserForEntity(
            @Valid @RequestBody Mono<User> userMono
    ){

        return userService.createNewUser(userMono)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

*/







}
