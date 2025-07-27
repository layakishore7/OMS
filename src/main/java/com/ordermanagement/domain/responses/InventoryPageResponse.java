package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.responseDTO.InventoryResponse;
import com.ordermanagement.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryPageResponse {

    private List<InventoryResponse> content;

    private MetaData metaData;
}
