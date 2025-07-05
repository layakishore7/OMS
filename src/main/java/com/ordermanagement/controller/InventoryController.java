package com.ordermanagement.controller;

import com.ordermanagement.domain.requestDTO.InventoryRequest;
import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.entity.Inventory;
import com.ordermanagement.repository.InventoryRepository;
import com.ordermanagement.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @GetMapping("/inventory")
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @PostMapping("/inventory")
    public ResponseEntity<InventoryResponse> addInventory(@RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.addInventory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("inventory/{inventoryId}")
    public ResponseEntity<InventoryResponse> getStockById(@PathVariable Integer inventoryId){
        InventoryResponse response = inventoryService.getStockById(inventoryId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("inventory/{inventoryId}")
    public ResponseEntity<InventoryResponse> updateInventory(@PathVariable Integer inventoryId,@RequestBody InventoryRequest request) {
        InventoryResponse updatedInventory = inventoryService.updateInventory(inventoryId,request);
        return new ResponseEntity<>(updatedInventory,HttpStatus.OK);
    }

    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Integer inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }




}
