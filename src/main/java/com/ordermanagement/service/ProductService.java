package com.ordermanagement.service;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.dto.ProductRequest;
import com.ordermanagement.dto.ProductResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }


    public ProductResponse addProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStock(request.getStock());
        product.setStatus(Enum.ProductStatus.ACTIVE);
        product.setCategory(request.getCategory());


        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        ProductResponse response = new ProductResponse();
        response.setName(savedProduct.getName());
        response.setSku(savedProduct.getSku());
        response.setCategoryId(savedProduct.getCategory().getId());
        response.setPrice(savedProduct.getPrice());
        response.setDescription(savedProduct.getDescription());
        response.setStock(savedProduct.getStock());
        response.setCategory(savedProduct.getCategory());

        return response;
    }

    public ProductResponse getProductById(Integer productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(product.getName());
        productResponse.setSku(product.getSku());
        productResponse.setCategoryId(product.getCategory().getId());
        productResponse.setPrice(product.getPrice());
        productResponse.setStock(product.getStock());
        productResponse.setDescription(product.getDescription());
        productResponse.setStatus(Enum.ProductStatus.ACTIVE);
        productResponse.setCategory(product.getCategory());

        return productResponse;
    }


    public Product updateProduct(Integer productId, ProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Integer productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
            ResponseEntity.noContent().build();
        } else {
            ResponseEntity.notFound().build();
        }
    }

}
