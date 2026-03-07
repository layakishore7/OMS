package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.CategoryRequest;
import com.ordermanagement.domain.responseDTO.CategoryResponse;
import com.ordermanagement.domain.responses.CategoriesPageResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<APIResponse> addCategory(@RequestBody CategoryRequest categoryRequest) {
        List<CategoryResponse> responses = categoryService.addProductCategory(categoryRequest);
        return APIResponse.created("Category Created Successfully", responses);
    }

    @PutMapping("/categories/{categoryId}")
    public ResponseEntity<APIResponse> updateCategory(@RequestBody CategoryRequest categoryRequest, @PathVariable Integer categoryId) {
        CategoryResponse response = categoryService.updateCategory(categoryRequest, categoryId);
        return APIResponse.updated("Category Updated Successfully", response);
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<APIResponse> deleteCategory(@PathVariable Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return APIResponse.success("Category Deleted Successfully");
    }

    @GetMapping("/categories")
    public ResponseEntity<APIResponse> getAllCategories(
            @RequestParam("search") String search,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        List<CategoryResponse> responses = categoryService.getAllCategories(search, pageNumber, pageSize);
        return APIResponse.success("Categories Fetched Successfully", responses);
    }


}

