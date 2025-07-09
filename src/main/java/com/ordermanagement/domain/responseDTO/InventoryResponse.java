package com.ordermanagement.domain.responseDTO;


import com.ordermanagement.Enum.Enum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {

    private Integer inventoryId;

    private Integer productId;

    private String referenceNumber;

    private Integer quantity;

    private LocalDateTime receivedDate;

    private String warehouseName;

    private String storageLocation;

    private String palletNumber;

    private Enum.StorageType storageType;

    private Enum.InventoryStatus inventoryStatus;

    private LocalDate expiryDate;

    private Enum.Status status;

    private String reason;

    private LocalDateTime shipmentDate;

    private LocalDateTime deliveryDate;

}
