package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.CategoryRequest;
import com.ordermanagement.domain.responseDTO.CategoryResponse;
import com.ordermanagement.entity.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {

    public Category requestToEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        return Category.builder()
                .categoryName(request.getCategoryName())
                .description(request.getDescription())
                //.status(Enum.Status.ACTIVE)
                .build();
    }

    public CategoryResponse entityToResponse(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .status(category.getStatus())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
