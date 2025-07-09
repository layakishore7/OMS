package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.InventoryRequest;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.entity.Inventory;
import com.ordermanagement.entity.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryMapper {


    public Inventory requestToEntity(InventoryRequest request,Product product) {
        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setWarehouseName(request.getWarehouseName());
        inventory.setStorageLocation(request.getStorageLocation());
        inventory.setStorageLocation(request.getStorageLocation());
        inventory.setPalletNumber(request.getPalletNumber());
        inventory.setStorageType(request.getStorageType());
        inventory.setExpiryDate(request.getExpiryDate());
        inventory.setStatus(Enum.Status.ACTIVE);
        inventory.setReferenceNumber(request.getReferenceNumber());
        inventory.setInventoryStatus(request.getInventoryStatus());
        inventory.setReason(request.getReason());
        inventory.setReceivedDate(LocalDateTime.now());
        return inventory;
    }

    public InventoryResponse entityToResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.setInventoryId(inventory.getId());
        response.setProductId(inventory.getProduct().getId());
        response.setQuantity(inventory.getQuantity());
        response.setWarehouseName(inventory.getWarehouseName());
        response.setStorageLocation(inventory.getStorageLocation());
        response.setPalletNumber(inventory.getPalletNumber());
        response.setStorageType(inventory.getStorageType());
        response.setExpiryDate(inventory.getExpiryDate());
        response.setStatus(Enum.Status.ACTIVE);
        response.setReferenceNumber(inventory.getReferenceNumber());
        response.setInventoryStatus(inventory.getInventoryStatus());
        response.setReason(inventory.getReason());
        response.setReceivedDate(inventory.getReceivedDate());
        return response;
    }
    

    public void updateEntityFromRequest(Inventory inventory, InventoryRequest request) {
       inventory.setQuantity(request.getQuantity());
       inventory.setWarehouseName(request.getWarehouseName());
       inventory.setStorageLocation(request.getStorageLocation());
       inventory.setStorageType(request.getStorageType());
       inventory.setPalletNumber(request.getPalletNumber());
       inventory.setExpiryDate(request.getExpiryDate());
       inventory.setReferenceNumber(request.getReferenceNumber());
       inventory.setInventoryStatus(request.getInventoryStatus());
    }
}
