package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsPageResponse {

    private List<Product> content;

    private MetaData metaData;
}
