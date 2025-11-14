package com.ordermanagement.domain.responseDTO;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Product;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemResponse {

    private Product product;

    private String itemId;

    private Long pieces;

    @Enumerated(EnumType.ORDINAL)
    private com.ordermanagement.Enum.Enum.OrderStatus itemStatus;

    private String referenceNumber;

    private LocalDateTime shipmentDate;

    private String warehouseName;

    private String storageLocation;

    private String palletNumber;

    @Enumerated(EnumType.STRING)
    private com.ordermanagement.Enum.Enum.StorageType storageType;

    @Enumerated(EnumType.STRING)
    private Enum.InventoryStatus inventoryStatus;

    private LocalDate expiryDate;

    private Integer quantity;

    private Double price;
}
