package com.ordermanagement.entity;


import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    private String name;

    private String sku;

    private Integer price;

    private Integer stock;

    private String description;

    @Enumerated(EnumType.STRING)
    private Enum.ProductStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
