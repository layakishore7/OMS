package com.ordermanagement.domain.requestDTO;

import com.ordermanagement.Enum.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierShipperWarehouseAssociationLinkDto {

    private Integer shipperId;
    private Integer warehouseId;
    private String association;

}
