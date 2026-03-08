package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.CategoryRequest;
import com.ordermanagement.domain.responseDTO.CategoryResponse;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<APIResponse> updateCategory(@RequestBody CategoryRequest categoryRequest,
            @PathVariable Integer categoryId) {
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
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "shipperId", required = false) Integer shipperId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize) {
        List<CategoryResponse> responses = categoryService.getAllCategories(search, shipperId, pageNumber, pageSize);
        return APIResponse.success("Categories Fetched Successfully", responses);
    }

    @GetMapping("/categories/load-all")
    public ResponseEntity<APIResponse> fetchAllCategories() {
        List<CategoryResponse> responses = categoryService.fetchAllCategories();
        return APIResponse.success("Categories Fetched Successfully", responses);
    }

    @GetMapping("/categories/shipper/{shipperId}")
    public ResponseEntity<APIResponse> getAllCategoriesByShipper(
            @PathVariable("shipperId") Integer shipperId) {

        try {
            List<CategoryResponse> categories = categoryService.getAllCategoriesByShipperId(shipperId);
            return APIResponse.success(categories);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
