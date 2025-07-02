package com.ordermanagement.dto;

import com.ordermanagement.entity.Category;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {


    private String name;

    private String sku;

    private Integer categoryId;

    private Integer price;

    private String description;

    private Integer stock;

    private Enum status;

    private Category category;

}
