package com.ordermanagement.domain.responseDTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {

    private Integer productId;

    private String productName;

    private Integer shipperId;

    private String description;

    private Integer categoryId;

    private Double length;

    private Double breadth;

    private Double height;

    private String dimensionUom;

    private Double weight;

    private String weightUom;

    private Boolean serializable;

    private Integer availableQty;

    private String uploadImage;
}