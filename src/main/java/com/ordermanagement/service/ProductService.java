package com.ordermanagement.service;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.dto.ProductRequest;
import com.ordermanagement.dto.ProductResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        product.setQuantity(request.getQuantity());
        product.setStatus(Enum.ProductStatus.ACTIVE);

        // Fetch and set the Category using categoryId from the request
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        ProductResponse response = new ProductResponse();
        response.setName(savedProduct.getName());
        response.setSku(savedProduct.getSku());
        response.setCategoryId(savedProduct.getCategory().getCategoryId());
        response.setPrice(savedProduct.getPrice());
        response.setDescription(savedProduct.getDescription());
        response.setStock(savedProduct.getQuantity());

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
        productResponse.setCategoryId(product.getCategory().getCategoryId());
        productResponse.setPrice(product.getPrice());
        productResponse.setStock(product.getQuantity());
        productResponse.setStatus(product.getStatus());

        return productResponse;
    }
}
