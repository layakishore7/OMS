package com.ordermanagement.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product extends BaseEntity {

    @Column(name = "product_name")
    private String productName;

    private String description;

    private String productUniqueId;

    private Integer availableQuantity;

    private Double length;

    private Double breadth;

    private Double height;

    private String dimensionUom;

    private Double weight;

    private String weightUom;

    private Boolean serializable;

    private String uploadImage;

    @ManyToOne
    @JoinColumn(name = "shipper_id",nullable = false)
    private Organization organization;


    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
}
