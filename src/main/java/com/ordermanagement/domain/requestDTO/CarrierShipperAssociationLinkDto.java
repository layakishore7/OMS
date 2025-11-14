package com.ordermanagement.domain.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierShipperAssociationLinkDto {

    Integer shipperId;
    String association;
}
