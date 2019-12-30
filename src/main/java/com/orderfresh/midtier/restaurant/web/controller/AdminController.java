package com.orderfresh.midtier.restaurant.web.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/restaurant", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminController {
}

