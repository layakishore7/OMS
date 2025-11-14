package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesPageResponse {

    private List<Category> content;

    private MetaData metaData;
}
