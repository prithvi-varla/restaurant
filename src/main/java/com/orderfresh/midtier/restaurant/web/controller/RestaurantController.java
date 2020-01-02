package com.orderfresh.midtier.restaurant.web.controller;

import com.orderfresh.midtier.restaurant.model.RestaurantAdminInfo;
import com.orderfresh.midtier.restaurant.repository.model.OrderDTO;
import com.orderfresh.midtier.restaurant.service.CompanyService;
import com.orderfresh.midtier.restaurant.web.model.AdminInfo;
import com.orderfresh.midtier.restaurant.web.model.Company;
import com.orderfresh.midtier.restaurant.web.model.MenuItem1;
import com.orderfresh.midtier.restaurant.web.model.Order1;
import com.orderfresh.midtier.restaurant.web.model.User;
import com.orderfresh.midtier.restaurant.security.model.ApiPrincipal;
import com.orderfresh.midtier.restaurant.security.model.AuthRequest;
import com.orderfresh.midtier.restaurant.security.model.AuthResponse;
import com.orderfresh.midtier.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import com.orderfresh.midtier.restaurant.web.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/restaurant", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RestaurantController {

    Logger logger = LoggerFactory.getLogger("mylog");

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyService companyService;


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

    //@RequestMapping(value = "/v1/admin/login", method = RequestMethod.POST)
    @PostMapping(
            value = {"/v1/admin/login"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {

        Mono<AuthResponse> result = userService.getUserLoginInfo(ar);

        return result.map(resp -> new ResponseEntity<>(
                                  resp,
                                  HttpStatus.OK
                          )
        )
                     .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                                   .body(null));
    }

    @PostMapping(value = "/v1/admin/user",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> createUserForEntity(
            @Valid @RequestBody Mono<User> userMono
    ){

        return userService.createNewUser(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    @PostMapping(value = "/v1/restaurants/create")
    public Mono<ResponseEntity<Company>> createCompanyEntity(
            @Valid @RequestBody Mono<Company> companyMono
    ){

        return companyService.createCompany(companyMono)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }



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



    @PostMapping(value = "/v1/restaurants/menuitem")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<MenuItem1>> createMenuItem(
            @Valid @RequestBody Mono<MenuItem1> order1Mono,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return userService.createMenuItem(order1Mono, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    @GetMapping(value = "/v1/restaurants/adminInfo1")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<AdminInfo>> getRestaurantAdminInfo(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return userService.getAdminInfo(apiPrincipal)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }



    @GetMapping(value = "/v1/restaurants/allorders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<AdminInfo>> getRestaurantAllOrders(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return userService.getAllOrdersAdminInfo(apiPrincipal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }





    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<?>> user(@AuthenticationPrincipal ApiPrincipal principal) {
        return Mono.just(ResponseEntity.ok(new Message("Content for user")));
    }


}