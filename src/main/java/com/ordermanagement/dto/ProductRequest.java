package com.ordermanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private String sku;
    private Integer categoryId;
    private Integer price;
    private String description;
    private Integer quantity;
    private String status;  // Will map to Enum.ProductStatus


}
