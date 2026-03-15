package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Organization;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductMapper {


    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    // Convert ProductRequest to Product entity
    public Product requestToEntity(ProductRequest request, Integer organizationId) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        Organization organization = organizationRepository.findById(organizationId).orElseThrow();
        product.setOrganization(organization);
        Category category = categoryRepository.findByCategoryNameAndStatus(request.getCategoryName()).orElseThrow();
        product.setCategory(category);
        product.setLength(request.getLength());
        product.setBreadth(request.getBreadth());
        product.setHeight(request.getHeight());
        product.setDimensionUom(request.getDimensionUom());
        product.setWeight(request.getWeight());
        product.setWeightUom(request.getWeightUom());
        product.setSerializable(request.getSerializable());
        product.setAvailableQuantity(0);
        // Set ProductUniqueId: use from request if provided, else auto-generate
        if (request.getProductUniqueId() != null && !request.getProductUniqueId().isBlank()) {
            product.setProductUniqueId(request.getProductUniqueId());
        } else {
            String uniqueId = organization.getOrganizationCode() + "-"
                    + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            product.setProductUniqueId(uniqueId);
        }
        product.setStatus(Enum.Status.ACTIVE);
        return product;
    }

    public ProductResponse entityToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getId());
        response.setProductUniqueId(product.getProductUniqueId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setShipperId(product.getOrganization().getId());
        response.setCategoryId(product.getCategory().getId());
        response.setLength(product.getLength());
        response.setBreadth(product.getBreadth());
        response.setHeight(product.getHeight());
        response.setDimensionUom(product.getDimensionUom());
        response.setWeight(product.getWeight());
        response.setWeightUom(product.getWeightUom());
        response.setSerializable(product.getSerializable());
        response.setAvailableQty(product.getAvailableQuantity());
        response.setUploadImage(product.getUploadImage());
        return response;
    }

    public void updateEntityFromRequest(Product product, ProductRequest request, Organization organization,
            Category category) {
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setOrganization(organization);
        product.setCategory(category);
        product.setLength(request.getLength());
        product.setBreadth(request.getBreadth());
        product.setHeight(request.getHeight());
        product.setDimensionUom(request.getDimensionUom());
        product.setWeight(request.getWeight());
        product.setWeightUom(request.getWeightUom());
        product.setSerializable(request.getSerializable());
        product.setProductUniqueId(request.getProductUniqueId());
        product.setStatus(Enum.Status.ACTIVE);
    }

    public ProductResponse UpdateEntityToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getId());
        response.setProductUniqueId(product.getProductUniqueId());
        response.setProductName(product.getProductName());
        response.setDescription(product.getDescription());
        response.setShipperId(product.getOrganization().getId());
        response.setCategoryId(product.getCategory().getId());
        response.setLength(product.getLength());
        response.setBreadth(product.getBreadth());
        response.setHeight(product.getHeight());
        response.setDimensionUom(product.getDimensionUom());
        response.setWeight(product.getWeight());
        response.setWeightUom(product.getWeightUom());
        response.setSerializable(product.getSerializable());
        response.setAvailableQty(product.getAvailableQuantity());
        return response;
    }

    public ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getId());
        response.setProductUniqueId(product.getProductUniqueId());
        response.setProductName(product.getProductName());
        response.setShipperId(product.getOrganization().getId());
        response.setCategoryId(product.getCategory().getId());
        response.setDescription(product.getDescription());
        response.setLength(product.getLength());
        response.setBreadth(product.getBreadth());
        response.setHeight(product.getHeight());
        response.setDimensionUom(product.getDimensionUom());
        response.setWeight(product.getWeight());
        response.setWeightUom(product.getWeightUom());
        response.setSerializable(product.getSerializable());
        response.setAvailableQty(product.getAvailableQuantity());
        response.setUploadImage(product.getUploadImage());
        return response;
    }

}
