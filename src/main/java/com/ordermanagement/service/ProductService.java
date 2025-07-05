package com.ordermanagement.service;
import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.mapper.ProductMapper;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.exceptions.DeletionException;
import com.ordermanagement.exceptions.RecordNotFoundException;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductMapper productMapper;

    public List<Product> getAllProducts(){
        return productRepository.fetchAllProducts();
    }


    public ProductResponse addProduct(ProductRequest request) {

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.requestToEntity(request,category);

        Product savedProduct = productRepository.save(product);

        return productMapper.entityToResponse(savedProduct);
    }


    public ProductResponse getProductById(Integer productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
       return productMapper.entityToResponse(product);
    }


    public ProductResponse updateProduct(Integer productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
       productMapper.updateEntityFromRequest(product,request);
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.entityToResponse(savedProduct);
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new RecordNotFoundException("Product Not Found"));
        if (product.getStock().equals(0)){
            product.setStatus(Enum.Status.INACTIVE);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        } else {
            throw new DeletionException("Product Can't be deleted with Stock");
        }
    }

}
