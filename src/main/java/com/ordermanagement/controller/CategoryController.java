package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
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

    @GetMapping("/categories")
    public ResponseEntity<APIResponse> getAllCategories(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int size) {
        try {
            CategoriesPageResponse response = categoryService.getCategory(search,pageNumber,size);
            return APIResponse.success(response);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Integer id,
                                                   @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}

