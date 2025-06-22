package com.ordermanagement.controller;

import com.ordermanagement.entity.Category;
import com.ordermanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("getAll")
    public List<Category> getAllCategories(){
       return categoryRepository.findAll();
    }
}

