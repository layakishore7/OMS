package com.ordermanagement.domain.responseDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrierShipperWarehouseAssociationResponseDto {

    private Integer carrierId;
    private Integer shipperId;
    private Integer warehouseId;
    private String association;
}
