package com.ordermanagement.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer inventoryId;

    private Integer quantity;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
