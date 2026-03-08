package com.ordermanagement.domain.responseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {

    private Integer productId;

    @JsonProperty("productUniqueId")
    private String productUniqueId;

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