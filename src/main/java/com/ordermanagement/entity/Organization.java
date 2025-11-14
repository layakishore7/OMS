package com.ordermanagement.entity;


import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "organizations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Organization extends BaseEntity {

    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "organization_code")
    private String organizationCode;

    @Enumerated(EnumType.ORDINAL)
    private Enum.OrganizationType organizationType;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "organization_contact")
    private String organizationContact;

    @Column(name = "association")
    private Enum.Status association;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String country;

    @Column(name = "is_inventory")
    private boolean isInventory;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Enum.Status status = Enum.Status.ACTIVE;

}
