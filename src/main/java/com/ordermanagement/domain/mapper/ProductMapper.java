package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    //Convert ProductRequest to Product entity
    public Product requestToEntity(ProductRequest request, Category category) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setSku(request.getSku());
        product.setCategory(category);
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setStatus(Enum.Status.ACTIVE);
        return product;
    }

    public ProductResponse entityToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductName(product.getProductName());
        response.setSku(product.getSku());
        response.setCategory(product.getCategory());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setDescription(product.getDescription());
        response.setStatus(product.getStatus());
        return response;
    }

    public void updateEntityFromRequest(Product product, ProductRequest request) {
        product.setProductName(request.getProductName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
    }


}
