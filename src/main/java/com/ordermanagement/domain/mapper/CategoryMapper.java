package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.CategoryRequest;
import com.ordermanagement.domain.responseDTO.CategoryResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Organization;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class CategoryMapper {

    public Category convertCategoryRequestToEntity(CategoryRequest categoryRequest, String categoryName, Organization shipperOrganization){
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setStatus(Enum.Status.ACTIVE);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        category.setShipperOrganization(shipperOrganization);
        return category;
    }



    public CategoryResponse convertEntityToCategoryResponse(Category category){
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryId(category.getId());
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setShipperId(category.getShipperOrganization().getId());
        categoryResponse.setParentCategoryId(
                category.getParentCategory() != null ? category.getParentCategory().getId() : null
        );
        categoryResponse.setParentCategoryName(
                category.getParentCategory() != null ? category.getParentCategory().getCategoryName() : null
        );
        return categoryResponse;
    }


}
