package com.ordermanagement.domain.requestDTO;

import com.ordermanagement.Enum.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {

    private String organizationName;
    private String organizationCode;
    private com.ordermanagement.Enum.Enum.OrganizationType organizationType;
    private String contactEmail;
    private String organizationContact;
    private String address;
    private String state;
    private String city;
    private String country;
    private String zipCode;
    private boolean inventory;
    private Enum.OrganizationType status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
