package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsPageResponse {

    private List<ProductResponse> content;

    private MetaData metaData;
}
