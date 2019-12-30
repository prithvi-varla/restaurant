package com.orderfresh.midtier.restaurant.web.controller;

import com.orderfresh.midtier.restaurant.handler.ImageHandler;
import com.orderfresh.midtier.restaurant.model.Customer;
import com.orderfresh.midtier.restaurant.model.CustomerInfo;
import com.orderfresh.midtier.restaurant.model.MenuItem;
import com.orderfresh.midtier.restaurant.model.Order;
import com.orderfresh.midtier.restaurant.model.Person;
import com.orderfresh.midtier.restaurant.model.Person2;
import com.orderfresh.midtier.restaurant.model.Promotion;
import com.orderfresh.midtier.restaurant.model.Restaurant;
import com.orderfresh.midtier.restaurant.model.RestaurantAdminInfo;
import com.orderfresh.midtier.restaurant.model.Review;
import com.orderfresh.midtier.restaurant.model.User;
import com.orderfresh.midtier.restaurant.security.JWTUtil;
import com.orderfresh.midtier.restaurant.security.PBKDF2Encoder;
import com.orderfresh.midtier.restaurant.security.model.AuthRequest;
import com.orderfresh.midtier.restaurant.security.model.AuthResponse;
import com.orderfresh.midtier.restaurant.service.RestaurantService;
import com.orderfresh.midtier.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CustomerController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PBKDF2Encoder passwordEncoder;

    @Autowired
    private UserService userRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    ImageHandler imageHandler;

    @PostMapping(value = "/restaurant/v1/customers")
    public Mono<ResponseEntity<Customer>> createCustomerEntity(
            @Valid @RequestBody Mono<Customer> customerMono
    ){

        return restaurantService.createCustomer(customerMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    @GetMapping(value = "/restaurant/v1/customers/{id}")
    public Mono<ResponseEntity<Customer>> getCustomerEntity(
            @Valid @PathVariable UUID id
    ){

        return restaurantService.getCustomer(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @PostMapping(value = "/restaurant/v1/customersInfo")
    public Mono<ResponseEntity<CustomerInfo>> createCustomerInfo(
            @Valid @RequestBody Mono<CustomerInfo> customerInfoMono
    ){

        return restaurantService.createCustomerInfo(customerInfoMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));

    }

    @GetMapping(value = "/restaurant/v1/customerInfo/{id}")
    public Mono<ResponseEntity<CustomerInfo>> getCustomerInfoEntity(
            @Valid @PathVariable UUID id
    ){

        return restaurantService.getCustomerInfo(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @GetMapping(value = "/restaurant/v1/restaurants")
    public Mono<ResponseEntity<List<Restaurant>>> getRestaurants(
    ){

        return restaurantService.getRestaurants()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @GetMapping(value = "/restaurant/v1/restaurants/{id}/menu_items")
    public Mono<ResponseEntity<List<MenuItem>>> getMenuItems(
            @Valid @PathVariable String id
    ){

        return restaurantService.getMenuItems()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }



    /*@GetMapping(value = "/restaurant/v1/restaurants/{id}/menu_items")
    public Mono<ResponseEntity<List<MenuItem>>> getMenuItems(
            @Valid @PathVariable String id
    ){

        return restaurantService.getMenuItems()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }*/



    @PostMapping(value = "/restaurant/v1/users")
    public Mono<ResponseEntity<User>> createUserForEntity(
            @Valid @RequestBody Mono<User> userMono
    ){

        return restaurantService.createUser(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*@PostMapping(value = "/restaurant/v1/users/session")
    public Mono<ResponseEntity<User>> getUserForEntity(
            @Valid @RequestBody Mono<User> userMono
    ){

        return restaurantService.loginUser(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(200).body(null));
    }*/


    @DeleteMapping(value = "/restaurant/v1/users/session")
    public Mono<ResponseEntity> deleteFromEntity(){

        restaurantService.deleteUser();
        return Mono.just(new ResponseEntity<>(null, HttpStatus.OK));
    }


    @PostMapping(value = "/restaurant/v1/orders")
    public Mono<ResponseEntity<Order>> createOrdersForEntity(
            @Valid @RequestBody Mono<Order> userMono
    ){

        return restaurantService.createOrder(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    @PostMapping(value = "/restaurant/v1/reviews")
    public Mono<ResponseEntity<Review>> createReviewsForEntity(
            @Valid @RequestBody Mono<Review> userMono
    ){

        return restaurantService.createReview(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }





    /*@PostMapping(value = "/restaurant/v1/promotions1",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<Promotion> saveImage(@RequestParam("title") String title, @RequestPart("file") Mono<FilePart> part) throws Exception{

        new Binary(BsonBinarySubType.BINARY, file.getBytes());
        return part.flatMap(file -> imageService.saveImage(file));
    }*/


    @PostMapping(value = "/restaurant/v1/promotions")
    public Mono<ResponseEntity<Promotion>> createPromotion(
            @Valid @RequestBody Mono<Promotion> userMono
    ){

        return restaurantService.createPromotion(userMono)
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

        return restaurantService.getPromotions(id)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    @PostMapping(value = "/restaurant/v1/promotions/photos123", consumes = { "multipart/form-data" })
    public Mono<Void> addPhoto123(@RequestPart("file") Flux<FilePart> file){
        //String id = photoService.addPhoto(title, image);
        //return imageHandler.saveImage(file);


        return restaurantService.createImage(file);

    }












    @PostMapping(value = "/restaurant/v1/persons")
    public Mono<ResponseEntity<Person>> createPersonForEntity(
            @Valid @RequestBody Mono<Person> userMono
    ){

        return restaurantService.createPerson(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    @PostMapping(value = "/restaurant/v1/persons/session")
    public Mono<ResponseEntity<Person>> getPersonForEntity(
            @Valid @RequestBody Mono<Person> userMono
    ){

        return restaurantService.loginPerson(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(402).body(null));
    }



    @PostMapping(value = "/restaurant/v1/persons2/session")
    public Mono<ResponseEntity<Person2>> getPerson2ForEntity(
            @Valid @RequestBody Mono<Person2> userMono
    ){

        return restaurantService.loginPerson2(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(402).body(null));
    }


    @CrossOrigin
    @GetMapping(value = "/restaurant/v1/restaurants/adminInfo")
    public Mono<ResponseEntity<RestaurantAdminInfo>> getRestaurantAdminInfo(
    ){

        return restaurantService.getRestaurantAdminInfo()
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


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

}
