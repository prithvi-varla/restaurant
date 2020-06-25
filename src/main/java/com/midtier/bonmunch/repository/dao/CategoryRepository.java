package com.midtier.bonmunch.repository.dao;

import com.midtier.bonmunch.repository.model.CategoryDTO;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CategoryRepository extends ReactiveMongoRepository<CategoryDTO, UUID> {

    Mono<CategoryDTO> findByCategoryId(UUID categoryId);

    @Query("{ 'categoryId': ?0, 'companyId': ?1}")
    Mono<CategoryDTO> findByCategoryIdAndCompanyId(UUID categoryId, UUID companyId);

    Flux<CategoryDTO> findByParentCategoryIdNotNullAndCompanyId( UUID companyId);

    Flux<CategoryDTO> findByParentCategoryIdNullAndCompanyId( UUID companyId);

    Flux<CategoryDTO> findByCompanyId(UUID categoryId);
}
