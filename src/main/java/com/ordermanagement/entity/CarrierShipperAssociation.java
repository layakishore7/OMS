package com.ordermanagement.entity;

import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "carrier_shipper_association")
@Entity
public class CarrierShipperAssociation extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Organization carrierOrganization;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Organization shipperOrganization;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Enum.Status carrierAssociationStatus;
}
