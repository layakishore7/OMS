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
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "pieces")
    private Long pieces;

    @Column(name = "quantity_received")
    private Integer quantityReceived;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="item_status")
    private com.ordermanagement.Enum.Enum.OrderStatus itemStatus;

    @Column(name = "reference_number")
    private String referenceNumber;

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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Enum.InventoryStatus inventoryStatus;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    private Integer quantity;

    private Double price;

}
