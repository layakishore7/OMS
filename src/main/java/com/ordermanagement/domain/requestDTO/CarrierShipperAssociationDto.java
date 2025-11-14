package com.ordermanagement.domain.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierShipperAssociationDto {

    List<CarrierShipperAssociationLinkDto> organizations;
}
