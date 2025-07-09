package com.ordermanagement.domain.requestDTO;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    private Integer quantity;

    private String warehouseName;

    private String storageLocation;

    private Enum.StorageType storageType;

    private String palletNumber;

    private LocalDate expiryDate;

    private String referenceNumber;

    private Enum.InventoryStatus inventoryStatus;

    private String reason;

    private Integer productId;

    private Enum.Status status;
}
