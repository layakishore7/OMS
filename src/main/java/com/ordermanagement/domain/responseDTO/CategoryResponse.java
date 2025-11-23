package com.ordermanagement.domain.responseDTO;

import com.ordermanagement.Enum.Enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Integer categoryId;

    private String categoryName;

    private Integer shipperId;

    private Integer parentCategoryId;

    private String parentCategoryName;
}
