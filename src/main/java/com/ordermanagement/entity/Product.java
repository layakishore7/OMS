package com.ordermanagement.entity;


import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseEntity {

    private String name;

    private String sku;

    private Integer price;

    private Integer stock;

    private String description;

    @Enumerated(EnumType.STRING)
    private Enum.ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
