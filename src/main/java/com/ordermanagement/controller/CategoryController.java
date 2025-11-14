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
    public ResponseEntity<Page<Category>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
            if (page<0) page =0;
            if ((size<1) || size>100) size = 10;

            if (!sortDirection.equalsIgnoreCase("asc")&&!sortDirection.equalsIgnoreCase("desc"))
                sortDirection = "asc";
            Page<Category> categories = categoryService.getAllCategories(page,size,sortBy,sortDirection);
            return ResponseEntity.ok(categories);
    }

    

    @PostMapping("/categories")
    public ResponseEntity<APIResponse> addCategory(@RequestBody Category category) {

        Category savedCategory = categoryService.addCategory(category);
        return APIResponse.created("Category Created Successfully",savedCategory);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<APIResponse> updateCategory(@PathVariable Integer id,
                                                   @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return APIResponse.updated("Category Updated Successfully",updatedCategory);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<APIResponse> deleteCategoryById(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return APIResponse.success("Category Deleted Successfully"); // 204 No Content
    }


}

