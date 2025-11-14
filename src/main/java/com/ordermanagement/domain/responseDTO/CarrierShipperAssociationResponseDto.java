package com.ordermanagement.domain.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrierShipperAssociationResponseDto {

    private Integer carrierId;
    private Integer shipperId;
    private String association;
}
