package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
        try {
            if (page<0) page =0;
            if ((size<1) || size>100) size = 10;

            if (!sortDirection.equalsIgnoreCase("asc")&&!sortDirection.equalsIgnoreCase("desc"))
                sortDirection = "asc";
            Page<Category> categories = categoryService.getAllCategories(page,size,sortBy,sortDirection);
            return APIResponse.success(categories);
        } catch (RuntimeException ex) {
            return APIResponse.error(ex.getMessage());
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

