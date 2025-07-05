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

    private String sku;

    private Integer categoryId;

    private Integer price;

    private String description;

    private Integer stock;

    private String status;

    private Category category;


}
