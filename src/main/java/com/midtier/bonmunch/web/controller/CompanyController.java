package com.midtier.bonmunch.web.controller;

import com.midtier.bonmunch.model.Customer;
import com.midtier.bonmunch.security.model.AuthRequest;
import com.midtier.bonmunch.security.model.AuthResponse;
import com.midtier.bonmunch.service.CompanyService;
import com.midtier.bonmunch.service.RestaurantService;
import com.midtier.bonmunch.service.UserService;
import com.midtier.bonmunch.web.model.Company;
import com.midtier.bonmunch.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/bonmunch", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CompanyController {

    @Autowired
    private UserService userService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    CompanyService companyService;

    /*
         Create new company (restaurant) endpoint
      */
    @PostMapping(value = "/v1/companies")
    public Mono<ResponseEntity<Company>> createCompany(
            @Valid @RequestBody Mono<Company> companyMono
    ){

        return companyService.createCompany(companyMono)
                             .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                             .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    /*
        Get customer/company information
     */
    @GetMapping(value = "/v1/companies/{id}")
    public Mono<ResponseEntity<Company>> getCompany(
            @Valid @PathVariable UUID id
    ){

        return companyService.getCompany(id)
                              .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                              .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }

    /*
        For creating user for entity endpoint (admin user)
     */
    @PostMapping(value = "/v1/companies/{id}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> createUser(
            @Valid @RequestBody Mono<User> userMono,
            @Valid @PathVariable UUID id
    ){

        return userService.createNewUser(userMono, id)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
    }

    /*
        Admin Login
     */
    @PostMapping(
            value = {"/v1/companies/login"},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest authRequest) {

        return userService.getUserLoginInfo(authRequest)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

}
