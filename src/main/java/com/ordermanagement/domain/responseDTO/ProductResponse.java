package com.ordermanagement.domain.responseDTO;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.ordermanagement.entity.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {


    private String productName;

    private String sku;

    private Integer price;

    private String description;

    private Integer stock;

    private Enum status;

    private Category category;
}