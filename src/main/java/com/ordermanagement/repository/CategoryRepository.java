package com.ordermanagement.repository;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    boolean existsByCategoryNameAndStatus(String categoryName, Enum.Status active);

    Optional<Category> findByCategoryNameAndStatus(String categoryName, Enum.Status active);

    @Query(value = "SELECT * FROM categories WHERE status = 1", nativeQuery = true)
    List<Category> fetchAllCategories();
}
