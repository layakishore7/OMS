package com.ordermanagement.domain.requestDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.ordermanagement.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String productName;

    private String productUniqueId;

    private Integer shipperId;

    private String description;

    @JsonProperty("categoryId")
    private Integer categoryId;

    private Double length;

    private Double breadth;

    private Double height;

    private String dimensionUom;

    private Double weight;

    private String weightUom;

    private Boolean serializable;

    private String uploadImage;

}
