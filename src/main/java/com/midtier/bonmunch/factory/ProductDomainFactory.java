package com.midtier.bonmunch.factory;

import com.midtier.bonmunch.repository.dao.UserRepository;
import com.midtier.bonmunch.repository.model.ItemOptionDTO;
import com.midtier.bonmunch.repository.model.ItemOptionSectionDTO;
import com.midtier.bonmunch.repository.model.ProductDTO;
import com.midtier.bonmunch.security.model.ApiPrincipal;
import com.midtier.bonmunch.web.model.ActionType;
import com.midtier.bonmunch.web.model.ItemOption;
import com.midtier.bonmunch.web.model.ItemOptionSection;
import com.midtier.bonmunch.web.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductDomainFactory {

    @Autowired
    UserRepository userRepository;


    /*
        Get Product Dto when saving the Product
     */
    public ProductDTO getProductDTOBuild(Product product, ActionType actionName, ApiPrincipal apiPrincipal) {

        UUID newProductId = UUID.randomUUID();
        return ProductDTO.builder()
                          .productId(ActionType.UPDATE.equals(actionName) ? product.getProductId() : newProductId)
                          .userId(apiPrincipal.getUserId())
                          .companyId(apiPrincipal.getCompanyId())
                          .categoryId(product.getCategoryId())
                          .productCategoryMapName(product.getProductCategoryMapName())
                          .name(product.getName())
                          .price(product.getPrice())
                          .description(product.getDescription())
                          .itemOptionSection(getItemOptionSectionDTOBuild(product.getItemOptionSection(),
                                                                          newProductId))
                          .createdDate(LocalDateTime.now())
                          .updatedDate(LocalDateTime.now())
                          .build();
    }

    public List<ItemOptionSectionDTO> getItemOptionSectionDTOBuild(List<ItemOptionSection>  itemOptionSections,
                                                                   UUID newProductId) {

        List<ItemOptionSectionDTO> itemOptionSectionDTOs = new ArrayList<>();
        if (itemOptionSections != null) {
            for (ItemOptionSection itemOptionSection: itemOptionSections){
                UUID itemOptionSectionId = UUID.randomUUID();
                itemOptionSectionDTOs.add(ItemOptionSectionDTO
                                                  .builder()
                                                  .itemOptionSectionId(itemOptionSectionId)
                                                  .productId(newProductId)
                                                  .name(itemOptionSection.getName())
                                                  .description(itemOptionSection.getDescription())
                                                  .required(itemOptionSection.isRequired())
                                                  .minAllowed(itemOptionSection.getMinAllowed())
                                                  .maxAllowed(itemOptionSection.getMaxAllowed())
                                                  .itemOption(getItemOptionDTOBuild(itemOptionSection.getItemOption(),
                                                                                    itemOptionSectionId))
                                                  .createdDate(LocalDateTime.now())
                                                  .updatedDate(LocalDateTime.now())
                                                  .build());
            }

        }

        return itemOptionSectionDTOs;
    }


    public List<ItemOptionDTO> getItemOptionDTOBuild(List<ItemOption> itemOptions, UUID itemOptionSectionId) {

        List<ItemOptionDTO> itemOptionDTOs = new ArrayList<>();
        for (ItemOption itemOption: itemOptions){
            itemOptionDTOs.add(ItemOptionDTO.builder()
                                            .itemOptionId(UUID.randomUUID())
                                            .itemOptionSectionId(itemOptionSectionId)
                                            .name(itemOption.getName())
                                            .description(itemOption.getDescription())
                                            .price(itemOption.getPrice())
                                            .createdDate(LocalDateTime.now())
                                            .updatedDate(LocalDateTime.now())
                                            .build());
        }
        return itemOptionDTOs;
    }

    /*
        Get Product Domain Object
     */
    public Product getProductBuild(ProductDTO productDTO) {
        return Product.builder()
                      .productId(productDTO.getProductId())
                      .userId(productDTO.getUserId())
                      .companyId(productDTO.getCompanyId())
                      .categoryId(productDTO.getCategoryId())
                      .productCategoryMapName(productDTO.getProductCategoryMapName())
                      .name(productDTO.getName())
                      .price(productDTO.getPrice())
                      .description(productDTO.getDescription())
                      .itemOptionSection(getItemOptionSectionBuild(productDTO.getItemOptionSection()))
                      .build();
    }

    public List<ItemOptionSection> getItemOptionSectionBuild(List<ItemOptionSectionDTO> itemOptionSectionDTOS) {

        List<ItemOptionSection> itemOptionSections = new ArrayList<>();
        for (ItemOptionSectionDTO itemOptionSectionDTO: itemOptionSectionDTOS){
            itemOptionSections.add(ItemOptionSection.builder()
                                                    .itemOptionSectionId(itemOptionSectionDTO.getItemOptionSectionId())
                                                    .menuItemId(itemOptionSectionDTO.getProductId())
                                                    .name(itemOptionSectionDTO.getName())
                                                    .description(itemOptionSectionDTO.getDescription())
                                                    .required(itemOptionSectionDTO.isRequired())
                                                    .minAllowed(itemOptionSectionDTO.getMinAllowed())
                                                    .maxAllowed(itemOptionSectionDTO.getMaxAllowed())
                                                    .itemOption(getItemOptionBuild(itemOptionSectionDTO.getItemOption()))
                                                    .build());
        }
        return itemOptionSections;
    }

    public List<ItemOption> getItemOptionBuild(List<ItemOptionDTO> itemOptionDTOS) {

        List<ItemOption> itemOptions = new ArrayList<>();
        for (ItemOptionDTO itemOptionDTO: itemOptionDTOS){
            itemOptions.add(ItemOption.builder()
                                      .itemOptionId(itemOptionDTO.getItemOptionId())
                                      .itemOptionSectionId(itemOptionDTO.getItemOptionSectionId())
                                      .name(itemOptionDTO.getName())
                                      .description(itemOptionDTO.getDescription())
                                      .price(itemOptionDTO.getPrice())
                                      .build());
        }
        return itemOptions;
    }


    /*
        Get all Products for given ProductDTOs
     */
    public List<Product> getAllProductBuild(List<ProductDTO> productDTOs) {

        List<Product> allItems = new ArrayList<>();

        productDTOs.stream()
                    .map(productDTO -> Product
                            .builder()
                            .productId(productDTO.getProductId())
                            .companyId(productDTO.getCompanyId())
                            .categoryId(productDTO.getCategoryId())
                            .productCategoryMapName(productDTO.getProductCategoryMapName())
                            .name(productDTO.getName())
                            .description(productDTO.getDescription())
                            .price(productDTO.getPrice())
                            .build())
                    .forEachOrdered(allItems::add);

        return allItems;

    }



}
