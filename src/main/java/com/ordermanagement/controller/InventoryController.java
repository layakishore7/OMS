package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.InventoryRequest;
import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.entity.Inventory;
import com.ordermanagement.repository.InventoryRepository;
import com.ordermanagement.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<APIResponse> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        if (page<0) page = 0;
        if (size<1 || size>100) size = 10;

        if (!sortDirection.equalsIgnoreCase("asc")&&!sortDirection.equalsIgnoreCase("desc"))
            sortDirection = "asc";
        try {
            Page<Inventory> inventory = inventoryService.getAllInventory(page, size, sortBy, sortDirection);
            return APIResponse.success(inventory);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }

    @GetMapping("/inventory/product/{productId}")
    public List<InventoryResponse> getInventoryByProductId(@PathVariable Integer productId) {
        return  inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping("/inventory")
    public ResponseEntity<APIResponse> addInventory(@RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.addInventory(request);
        return APIResponse.created("Inventory Added Successfully",response);
    }

    @GetMapping("inventory/{inventoryId}")
    public ResponseEntity<APIResponse> getInventoryById(@PathVariable Integer inventoryId){
        InventoryResponse response = inventoryService.getInventoryById(inventoryId);
        return APIResponse.success(response);
    }

    @PutMapping("inventory/{inventoryId}")
    public ResponseEntity<APIResponse> updateInventory(@PathVariable Integer inventoryId,@RequestBody InventoryRequest request) {
        InventoryResponse updatedInventory = inventoryService.updateInventory(inventoryId,request);
        return APIResponse.success("Inventory Updated Successfully",updatedInventory);
    }

    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<APIResponse> deleteProductById(@PathVariable Integer inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return APIResponse.success("Inventory Deleted Successfully");
    }
}
