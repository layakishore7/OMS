package com.ordermanagement.repository;

import com.ordermanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    @Query(value = "SELECT * FROM products WHERE status = 1 AND category_id = :categoryId", nativeQuery = true)
    List<Product> fetchProductsByCategory(Integer categoryId);

    @Query(value = "select * from products where status =1", nativeQuery = true)
    Page<Product> fetchAllProducts(String search, Pageable pageable);

    @Query(value = "select * from products where status =1 AND shipper_id = :shipperId", nativeQuery = true)
    List<Product> fetchProductsByShipperId(@Param("shipperId") Integer shipperId);


    @Query(value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
        FROM products p
        WHERE p.status = 1 
        AND p.product_name = :productName
        AND p.shipper_id = :shipperId
    """, nativeQuery = true)
    boolean existsByProductNameAndShipperId(
            @Param("productName") String productName,
            @Param("shipperId") Integer shipperId
    );

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
    FROM products p
    WHERE p.status = 1
    AND p.product_name = :productName
    AND p.shipper_id = :shipperId
    AND p.id != :productId
""", nativeQuery = true)
    boolean existsByProductNameAndShipperIdExcludingProduct(
            @Param("productName") String productName,
            @Param("shipperId") Integer shipperId,
            @Param("productId") Integer productId
    );
}