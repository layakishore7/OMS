package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.domain.responses.CategoriesPageResponse;
import com.ordermanagement.domain.responses.ProductsPageResponse;
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
    public ResponseEntity<APIResponse> getAllProducts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int  size){

            try {
                ProductsPageResponse products = productService.getAllProducts(search,pageNumber,size);
                return APIResponse.success(products);
            } catch (Exception e) {
                return APIResponse.error(HttpStatus.BAD_REQUEST,e.getMessage());
            }
    }


    @PostMapping("/products")
    public ResponseEntity<APIResponse> addProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.addProduct(request);
        return APIResponse.created("Product Created Successfully",response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable Integer productId) {
        ProductResponse response = productService.getProductById(productId);
        return APIResponse.success(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<APIResponse> updateProductById(@PathVariable Integer productId,@RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productService.updateProduct(productId,product);
        return APIResponse.updated("Product Updated Successfully",updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<APIResponse> deleteProductById(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return APIResponse.success("Product Deleted Successfully");
    }


}
