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
@Table(name = "organization_association")
@Entity
public class OrganizationAssociation extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Organization carrierOrganization;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Organization shipperOrganization;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Organization warehouseOrganization;

}
