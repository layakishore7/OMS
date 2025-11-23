package com.ordermanagement.domain.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    private String categoryName;

    private String parentCategoryName;

    private Integer shipperId;

    private Integer parentCategoryId;

}
