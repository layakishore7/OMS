package com.ordermanagement.service;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.mapper.InventoryMapper;
import com.ordermanagement.domain.requestDTO.InventoryRequest;
import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.entity.Inventory;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.InventoryRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InventoryMapper inventoryMapper;

    public List<Inventory> getAllInventory(){
        return inventoryRepository.fetchAllInventory();
    }

    public InventoryResponse addInventory(InventoryRequest request){

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new RuntimeException("Product Not Found"));

        Inventory inventory = inventoryMapper.requestToEntity(request,product);

        Inventory savedInventory = inventoryRepository.save(inventory);

        return inventoryMapper.entityToResponse(savedInventory);
    }

    public InventoryResponse getInventoryById(Integer inventoryId) {
        if (inventoryId == null) {
            throw new IllegalArgumentException("Inventory id cannot be null");
        }
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()->new RuntimeException("Inventory Not Found"));
        return inventoryMapper.entityToResponse(inventory);
    }

    public InventoryResponse updateInventory(Integer inventoryId,InventoryRequest inventory) {
        Inventory inv = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new RuntimeException("Inventory Not Found"));
        inventoryMapper.updateEntityFromRequest(inv,inventory);
        Inventory savedInventory = inventoryRepository.save(inv);
        return inventoryMapper.entityToResponse(savedInventory);
    }

    public void deleteInventory(Integer inventoryId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(()-> new RuntimeException("Inventory Not Found"));
        inventory.setStatus(Enum.Status.INACTIVE);
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    public List<InventoryResponse> getInventoryByProductId(Integer productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Product Id cannot be null");
        }
        List<Inventory> inventory = inventoryRepository.getInventoryByProductId(productId);

//        if (inventory.isEmpty()) {
//            throw new RuntimeException("Inventory Not Found");
//        }

        return inventory.stream()
                .map(inventoryMapper::entityToResponse)
                .collect(Collectors.toList());

    }

}
