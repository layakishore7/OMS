package com.ordermanagement.controller;

import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.entity.Product;
import com.ordermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;


    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){

            if (page<0) page = 0;
            if (size<1 || size>100) size = 10;

            if (!sortDirection.equalsIgnoreCase("asc")&&!sortDirection.equalsIgnoreCase("desc"))
                sortDirection = "asc";
            Page<Product> products = productService.getAllProducts(page, size, sortBy, sortDirection);
            return ResponseEntity.ok(products);
    }



    @PostMapping("/products")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.addProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer productId) {
        ProductResponse response = productService.getProductById(productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable Integer productId,@RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productService.updateProduct(productId,product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
