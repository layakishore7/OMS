package com.ordermanagement.repository;

import com.ordermanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    @Query(value = "SELECT * FROM products WHERE status = 1 AND category_id = :categoryId", nativeQuery = true)
    List<Product> fetchProductsByCategory(Integer categoryId);

    @Query(value = "select * from products where status =1",nativeQuery = true)
    Page<Product> fetchAllProducts(String search, Pageable pageable);
}
