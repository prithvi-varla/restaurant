package com.midtier.bonmunch.web.controller;

import com.midtier.bonmunch.model.ImageResult;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.service.AdminService;
import com.midtier.bonmunch.service.CategoryService;
import com.midtier.bonmunch.service.ImageService;
import com.midtier.bonmunch.service.ProductService;
import com.midtier.bonmunch.service.RestaurantService;
import com.midtier.bonmunch.service.SettingService;
import com.midtier.bonmunch.service.UserService;
import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.AdminSummary;
import com.midtier.bonmunch.web.model.Category;
import com.midtier.bonmunch.web.model.CategoryType;
import com.midtier.bonmunch.web.model.ImageType;
import com.midtier.bonmunch.web.model.Product;
import com.midtier.bonmunch.web.model.SiteSetting;

import com.midtier.bonmunch.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/bonmunch", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AdminController {

    Logger logger = LoggerFactory.getLogger("mylog");

    @Autowired
    private UserService userService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private ImageService imageService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProductService productService;

    /*
        Get all orders list in admin page
     */
    @GetMapping(value = "/v1/companies/users",
            params =  {"searchField"}
    )
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<User>> getUserProfile(
            @Valid @RequestParam(value = "searchField") String searchField,
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return userService.getUserForEntity(apiPrincipal, searchField)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    /*
        update user for entity endpoint (admin user)
     */
    @PutMapping(value = "/v1/companies/users",
            params =  {"isPasswordChanged"},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<User>> updateUserProfile(
            @Valid @RequestBody Mono<User> userMono,
            @Valid @RequestParam(value = "isPasswordChanged") boolean isPasswordChanged,
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return userService.updateUser(userMono, isPasswordChanged, apiPrincipal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    /*
        Get consolidated adminInfo on dashboard page
     */
    @GetMapping(value = "/v1/companies/adminsummary")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<AdminSummary>> getAdminSummaryInfo(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return adminService.getAdminSummaryInfo(apiPrincipal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    /*
        Create new product from admin page
     */
    @PutMapping(value = "/v1/companies/products",
            params =  {"action"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Product>> createProduct(
            @Valid @RequestBody Mono<Product> orderMono,
            @Valid @RequestParam(value = "action") ActionType actionName,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return productService.createProduct(orderMono, actionName, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Delete product in admin page
     */
    @DeleteMapping(value = "/v1/companies/products/{productId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteProduct(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @PathVariable UUID productId
    ){

        return productService.deleteProduct(productId, principal)
                          .map(resp -> new ResponseEntity<Void>( HttpStatus.OK));
    }


    /*
        Get all orders list in admin page for the company
     */
    @GetMapping(value = "/v1/companies/orders")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<AdminSummary>> getCompanyAllOrders(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal
    ){

        return adminService.getAllOrdersAdminInfo(apiPrincipal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(404).body(null));

    }

    /*
        Update siteSettings from admin page
     */
    @PutMapping(value = "/v1/companies/sitesettings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<SiteSetting>> updateSiteSettings(
            @Valid @RequestBody Mono<SiteSetting> siteSettingMono,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return settingService.createSiteSetting(siteSettingMono, principal)
                             .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                             .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    /*
        Create multiple image upload
     */
    @PostMapping(value = "/v1/companies/images",
            params =  {"imageName", "imageType"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Mono<ResponseEntity<ImageResult>> createMultipleGalleryImage(@RequestBody Flux<Part> parts ,
                                                                 @Valid @RequestParam(value = "imageName") String imageName,
                                                                 @Valid @RequestParam(value = "imageType") ImageType imageType,
                                                                 @AuthenticationPrincipal ApiPrincipal apiPrincipal) {

        return
                imageService.multipartUpload(parts, imageType, imageName, apiPrincipal)
                            .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                            .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }

    /*
        Delete a specific image
     */
    @DeleteMapping(value = "/v1/companies/images/{imageId}",
            params =  {"imageType"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Void>> deleteImage(
            @AuthenticationPrincipal ApiPrincipal apiPrincipal,
            @Valid @RequestParam(value = "imageType") ImageType imageType,
            @Valid @PathVariable UUID imageId
    ){

        return
                imageService.deleteImage(imageId, imageType, apiPrincipal)
                            .map(resp -> new ResponseEntity<Void>( HttpStatus.OK));
    }

    /*
        Create single image upload
     */
    @PostMapping(value = "/v1/companies/uploadSingleGalleryImage")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    Mono<ResponseEntity<ImageResult>> createSingleGalleryImage(@RequestHeader HttpHeaders headers, @RequestBody Flux<ByteBuffer> body) {

        return
                imageService.singleUpload(body, headers.getContentLength(), headers.getContentType())
                            .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                            .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Create new category from admin page
     */
    @PutMapping(value = "/v1/companies/categories",
            params =  {"action"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Category>> createCategory(
            @Valid @RequestBody Mono<Category> categoryMono,
            @Valid @RequestParam(value = "action") ActionType actionName,
            @AuthenticationPrincipal ApiPrincipal principal
    ){

        return categoryService.createCateogry(categoryMono, actionName, principal)
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Get a category from admin page
     */
    @GetMapping(value = "/v1/companies/categories/{categoryId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<Category>> getCategory(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @PathVariable UUID categoryId
    ){

        return categoryService.getCategory(categoryId, principal)
                              .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                              .defaultIfEmpty(ResponseEntity.status(400).body(null));
    }


    /*
        Get all category from admin page
     */
    @GetMapping(value = "/v1/companies/categories",
            params =  {"categoryType"})
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Mono<ResponseEntity<List<Category>>> getAllCategories(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @RequestParam(value = "categoryType") CategoryType categoryType
    ){

        return categoryService.getAllCategoriesWithNameMap(categoryType, principal.getCompanyId())
                          .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                          .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    /*
        Delete category in admin page
     */
    @DeleteMapping(value = "/v1/companies/categories/{categoryId}")
    public Mono<ResponseEntity<Void>> deleteCategory(
            @AuthenticationPrincipal ApiPrincipal principal,
            @Valid @PathVariable UUID categoryId
    ){

        return categoryService.deleteCategory(categoryId, principal)
                          .map(resp -> new ResponseEntity<Void>( HttpStatus.OK));
    }

}

