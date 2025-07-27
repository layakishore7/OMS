package com.ordermanagement.repository;

import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.domain.responses.InventoryPageResponse;
import com.ordermanagement.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Integer> {

    @Query(value = "SELECT * FROM inventory WHERE product_id = :productId and status = 1", nativeQuery = true)
    List<Inventory> getInventoryByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT * FROM inventory WHERE status = 1 ",nativeQuery = true)
    Page<Inventory> fetchAllInventory(String search, Pageable pageable);
}
