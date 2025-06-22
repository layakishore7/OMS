package com.ordermanagement.controller;

import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;


    @GetMapping("/getallproducts")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
