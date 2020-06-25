package com.midtier.bonmunch.service;

import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.Category;
import com.midtier.bonmunch.web.model.CategoryType;
import com.midtier.bonmunch.factory.CategoryDomainFactory;
import com.midtier.bonmunch.repository.dao.CategoryRepository;
import com.midtier.bonmunch.repository.model.CategoryDTO;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryDomainFactory categoryDomainFactory;

    public Mono<Category> createCateogry(Mono<Category> categoryMono, ActionType actionName, ApiPrincipal apiPrincipal) {

        return
                categoryMono.map(categoryObject -> categoryDomainFactory.getCategoryDTOBuild(categoryObject, actionName, apiPrincipal))
                            .publishOn(Schedulers.elastic())
                            .flatMap(categoryRepository::save)
                            .publishOn(Schedulers.elastic())
                            .subscribeOn(Schedulers.parallel())
                            .map(categoryDTO -> categoryDomainFactory.getCategoryBuild(categoryDTO));


    }

    public Mono<Category> getCategory(UUID categoryId, ApiPrincipal apiPrincipal) {

        return
                categoryRepository.findByCategoryIdAndCompanyId(categoryId, apiPrincipal.getCompanyId())
                                  .subscribeOn(Schedulers.elastic())
                                  .publishOn(Schedulers.parallel())
                                  .map(categoryDTO -> categoryDomainFactory.getCategoryBuild(categoryDTO));

    }

    // Can be used later to get all orders of an entity
    public Mono<List<CategoryDTO>> getAllCategoriesDTO(CategoryType categoryType, ApiPrincipal apiPrincipal) {

        Flux<CategoryDTO> categoryDTOFlux;

        switch (categoryType) {
            case CATEGORY:
                categoryDTOFlux = categoryRepository.findByParentCategoryIdNullAndCompanyId(apiPrincipal.getCompanyId());
                break;
            case SUB_CATEGORY:
                categoryDTOFlux = categoryRepository.findByParentCategoryIdNotNullAndCompanyId(apiPrincipal.getCompanyId());
                break;
            default:
                categoryDTOFlux = categoryRepository.findByCompanyId(apiPrincipal.getCompanyId());
        }
        return
            categoryDTOFlux
            .subscribeOn(Schedulers.elastic())
            .publishOn(Schedulers.parallel())
            .collectList();
    }

    private Mono<Map.Entry<UUID, String>> getNameFromUserId(CategoryDTO dto) {
        UUID tt = dto.getParentCategoryId();
        return categoryRepository.findByCategoryId(tt)
                             .map(t -> new AbstractMap.SimpleEntry<>(
                                     tt,
                                     t.getCategoryName()));
    }

    public Mono<List<Category>> getAllCategoriesWithNameMap(CategoryType categoryType, ApiPrincipal apiPrincipal) {

        List<Mono<?>> list = new ArrayList<>();

        Mono<List<CategoryDTO>> getAllCategoriesDTOs =
                getAllCategoriesDTO(categoryType, apiPrincipal);
        list.add(getAllCategoriesDTOs);

        // get Map of UUID and Name for top 5 orders
        Mono<Map<UUID, String>> NameMap =
                getAllCategoriesDTOs
                        .flatMapIterable(orderDTOList -> orderDTOList)
                        .flatMap(orderDTO ->  getNameFromUserId(orderDTO))
                        .collectMap( s -> s.getKey(), e -> e.getValue());
        list.add(NameMap);

        return
                Mono.zip(
                        list,
                        results -> categoryDomainFactory.getAllCategoriesInfo(results)
                );

    }

    public Mono<Void> deleteCategory(UUID categoryId, ApiPrincipal apiPrincipal) {

        return categoryRepository.deleteById(categoryId)
                                 .subscribeOn(Schedulers.elastic())
                                 .publishOn(Schedulers.parallel());

    }

}