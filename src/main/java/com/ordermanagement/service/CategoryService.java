package com.ordermanagement.service;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Product;
import com.ordermanagement.exceptions.CategoryDeletionException;
import com.ordermanagement.exceptions.RecordAlreadyExistsException;
import com.ordermanagement.exceptions.RecordNotFoundException;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;


    public Page<Category> getAllCategories(int page, int size, String sortBy, String sortDirection){

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending():
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page,size,sort);
        return categoryRepository.fetchAllCategories(pageable);

    }

    public List<Category> fetchAllCategories(){
        return categoryRepository.getAllCategories();
    }

    public ResponseEntity<Category> addCategory(Category category) {
        return Optional.ofNullable(category)
                .filter(cat-> ! categoryRepository.existsByCategoryNameAndStatus(cat.getCategoryName(), Enum.Status.ACTIVE))
                .map(categoryRepository::save)
                .map(savedCategory->ResponseEntity.status(HttpStatus.CREATED).body(savedCategory))
                .orElseThrow(()-> {
                    assert category != null;
                    return new RecordAlreadyExistsException("Category With Name "+category.getCategoryName()+" already exists");
                });
    }

    public Category updateCategory(Integer id, Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory-> {
                    categoryRepository.findByCategoryNameAndStatus(category.getCategoryName(), Enum.Status.ACTIVE)
                            .filter(existing-> !existing.getId().equals(id))
                            .ifPresent(existing-> {
                                throw new RecordAlreadyExistsException("Category With Name "+category.getCategoryName()+" already exists");
                            });
                    existingCategory.setCategoryName(category.getCategoryName());
                    existingCategory.setDescription(category.getDescription());
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(()-> new RecordNotFoundException("Category Not Found with id: "+id));
    }


    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new RecordNotFoundException("Category Not Found"));
        List<Product> activeProducts = productRepository.fetchProductsByCategory(id);

        if (!activeProducts.isEmpty()) {
            throw new CategoryDeletionException("Cannot deactivate category '" + category.getCategoryName() +
                    "'. " + activeProducts.size() + " active product(s) are still associated with this category");
        }
        category.setStatus(Enum.Status.INACTIVE);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }

}
