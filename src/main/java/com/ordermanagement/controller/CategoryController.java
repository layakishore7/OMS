package com.ordermanagement.controller;

import com.ordermanagement.entity.Category;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Category> getAllCategories(){
       return categoryService.getAllCategories();
    }

    @PostMapping("/categories")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    // Update category
    @PutMapping("/categories/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable Integer id,
            @RequestBody Category categoryData
    ) {
        Category updatedCategory = categoryService.updateCategory(
                id,
                categoryData.getName(),
                categoryData.getDescription()
        );
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}

