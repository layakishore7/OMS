package com.ordermanagement.entity;

import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "reference_number")
    private String referenceNumber;

    private Integer quantity;

    @Column(name = "shipment_date")
    private LocalDateTime shipmentDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "pallet_number")
    private String palletNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type")
    private Enum.StorageType storageType;

    @Column(name = "inventory_status")
    @Enumerated(EnumType.STRING)
    private Enum.InventoryStatus inventoryStatus;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Enum.Status status;

    private String reason;

}
