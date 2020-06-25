package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.Category;
import com.midtier.bonmunch.repository.dao.CategoryRepository;
import com.midtier.bonmunch.repository.model.CategoryDTO;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CategoryDomainFactory {

    @Autowired
    CategoryRepository categoryrRepository;

    /*
        Get Category Dto when saving the Category domain object
     */
    public CategoryDTO getCategoryDTOBuild(Category category, ActionType actionName, ApiPrincipal apiPrincipal) {

        UUID categoryId = UUID.randomUUID();
        return CategoryDTO.builder()
                          .categoryId(ActionType.UPDATE.equals(actionName) ? category.getCategoryId() : categoryId)
                          .companyId(apiPrincipal.getCompanyId())
                          .categoryName(category.getCategoryName())
                          .sortOrder(category.getSortOrder())
                          .categoryDescription(category.getCategoryDescription())
                          .parentCategoryId(category.getParentCategoryId())
                          .active(category.getActive())
                          .createdDate(LocalDateTime.now())
                          .updatedDate(LocalDateTime.now())
                          .build();
    }


    /*
        Get Category Domain Object
     */
    public Category getCategoryBuild(CategoryDTO categoryDTO) {
        return Category.builder()
                       .categoryId(categoryDTO.getCategoryId())
                       .companyId(categoryDTO.getCompanyId())
                       .categoryName(categoryDTO.getCategoryName())
                       .sortOrder(categoryDTO.getSortOrder())
                       .categoryDescription(categoryDTO.getCategoryDescription())
                       .parentCategoryId(categoryDTO.getParentCategoryId())
                       .active(categoryDTO.getActive())
                       .build();
    }


    /*
        GetAllOrderAdminInfo
     */
    public List<Category> getAllCategoriesInfo(Object[] results) {

        List<Category> allCategories = new ArrayList<>();

        int counter = 0;

        List<CategoryDTO> getCategories= (List<CategoryDTO>) results[counter++];
        Map<UUID, String> categoriesNameMap = (Map<UUID, String>) results[counter++];

        getCategories.stream()
                     .map(categoryDTO -> Category
                             .builder()
                             .categoryId(categoryDTO.getCategoryId())
                             .companyId(categoryDTO.getCompanyId())
                             .categoryName(categoryDTO.getCategoryName())
                             .sortOrder(categoryDTO.getSortOrder())
                             .categoryDescription(categoryDTO.getCategoryDescription())
                             .parentCategoryId(categoryDTO.getParentCategoryId())
                             .active(categoryDTO.getActive())
                             .categoryMapName(categoryDTO.getParentCategoryId() != null ?
                                                                    categoriesNameMap.get(
                                                                            categoryDTO.getParentCategoryId() )
                                                                            + " -> " +
                                                                             categoryDTO.getCategoryName() : categoryDTO.getCategoryName())
                             .build())
                     .forEachOrdered(allCategories::add);


        return allCategories;

    }

}
