package com.midtier.bonmunch.service;

import com.midtier.bonmunch.factory.ProductDomainFactory;
import com.midtier.bonmunch.repository.dao.ProductRepository;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.UUID;

/**
 * @author prithvi
 */
@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDomainFactory productDomainFactory;

    public Mono<Product> createProduct(Mono<Product> product, ActionType actionName, ApiPrincipal apiPrincipal) {

        return
                product.map(productObject -> productDomainFactory.getProductDTOBuild(productObject, actionName, apiPrincipal))
                        .publishOn(Schedulers.elastic())
                        .flatMap(productRepository::save)
                        .publishOn(Schedulers.elastic())
                        .subscribeOn(Schedulers.parallel())
                        .map(productDTO -> productDomainFactory.getProductBuild(productDTO));


    }

    public Mono<Product> getProduct(UUID productId, ApiPrincipal apiPrincipal) {

        return
                productRepository.findByProductIdAndcompanyId(productId, apiPrincipal.getCompanyId())
                                  .subscribeOn(Schedulers.elastic())
                                  .publishOn(Schedulers.parallel())
                                  .map(productDTO -> productDomainFactory.getProductBuild(productDTO));

    }

    public Mono<List<Product>> getAllProducts(ApiPrincipal apiPrincipal) {

        return
                productRepository.findByCompanyId(apiPrincipal.getCompanyId())
                                  .subscribeOn(Schedulers.elastic())
                                  .publishOn(Schedulers.parallel())
                                  .collectList()
                                  .map(productDTOs -> productDomainFactory.getAllProductBuild(productDTOs));

    }


    public Mono<Void> deleteProduct(UUID productId, ApiPrincipal apiPrincipal) {

        return productRepository.deleteById(productId)
                                 .subscribeOn(Schedulers.elastic())
                                 .publishOn(Schedulers.parallel());

    }
}
