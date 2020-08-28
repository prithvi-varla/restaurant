package com.midtier.bonmunch.web.controller;

import com.midtier.bonmunch.service.CategoryService;
import com.midtier.bonmunch.service.ProductService;
import com.midtier.bonmunch.web.model.Category;
import com.midtier.bonmunch.web.model.CategoryType;
import com.midtier.bonmunch.web.model.Image;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.web.model.Product;
import com.midtier.bonmunch.web.model.Review;
import com.midtier.bonmunch.service.ImageService;
import com.midtier.bonmunch.service.RestaurantService;
import com.midtier.bonmunch.service.SettingService;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.service.UserService;
import com.midtier.bonmunch.web.model.Order;
import com.midtier.bonmunch.web.model.SiteSetting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/bonmunch", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RestaurantController {

    Logger logger = LoggerFactory.getLogger("mylog");

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /*
        Get new product from admin page
     */
    @GetMapping(value = "/v1/companies/products/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Product>> getProduct(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @PathVariable UUID productId
    ){

        return productService.getProduct(productId, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(404).body(null));
    }

    /*
        Get all products from admin page
     */
    @GetMapping(value = "/v1/companies/products")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Product>>> getAllProducts(
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return productService.getAllProducts(principal.getCompanyId())
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    /*
        Get all products from company page
     */
    @GetMapping(value = "/v1/companies/{companyId}/products")
    public Mono<ResponseEntity<List<Product>>> getAllProductsForCompany(
            @Valid @PathVariable UUID companyId
    ){

        return productService.getAllProducts(companyId)
                             .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                             .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    /*
        Get all category from admin page
     */
    @GetMapping(value = "/v1/companies/{companyId}/categories",
            params =  {"categoryType"})
    public Mono<ResponseEntity<List<Category>>> getAllCategories(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @PathVariable UUID companyId,
            @Valid @RequestParam(value = "categoryType") CategoryType categoryType
    ){

        return categoryService.getAllCategoriesWithNameMap(categoryType, companyId)
                              .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                              .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /*
        Get all images for the restaurant for all pages
     */
    @GetMapping(
            value = "/v1/companies/images",
            params = {"page", "imageType"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Image>> getAllGalleryImages(
            @RequestHeader HttpHeaders headers,
            @RequestBody Flux<ByteBuffer> body,
            @Valid @RequestParam(value = "imageType") ImageType imageType,
            @Valid @RequestParam(value = "page") int page,
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return imageService.getAllGalleryImagesInfo(page, imageType, apiPrincipal.getCompanyId())
                           .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                           .defaultIfEmpty(ResponseEntity.status(400).body(null));

    }

    /*
        Get all images for the restaurant for all pages
     */
    @GetMapping(
            value = "/v1/companies/{id}/images",
            params = {"page", "imageType"}
    )
    public Mono<ResponseEntity<Image>> getAllGalleryImagesForCompany(
            @RequestHeader HttpHeaders headers,
            @RequestBody Flux<ByteBuffer> body,
            @Valid @RequestParam(value = "imageType") ImageType imageType,
            @Valid @RequestParam(value = "page") int page,
            @Valid @PathVariable UUID id
    ){

        return imageService.getAllGalleryImagesInfo(page, imageType, id)
                           .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                           .defaultIfEmpty(ResponseEntity.status(400).body(null));

    }

    /*
        Get site settings info
     */
    @GetMapping(value = "/v1/companies/sitesettings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<SiteSetting>> getSiteSetting(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return settingService.getSiteSetting(apiPrincipal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }


    /*
        Create review for the restaurant
     */
    @PostMapping(value = "/v1/companies/reviews")
    public Mono<ResponseEntity<Review>> createReview(
            @Valid @RequestBody Mono<Review> userMono
    ){

        return restaurantService.createReview(userMono)
                                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                                .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Create new order for the restaurant
     */
    @PostMapping(value = "/v1/companies/orders")
    public Mono<ResponseEntity<Order>> createOrder(
            @Valid @RequestBody Mono<Order> orderMono,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return userService.createOrder(orderMono, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

}