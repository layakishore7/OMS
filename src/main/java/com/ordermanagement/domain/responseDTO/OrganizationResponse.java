package com.ordermanagement.domain.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponse {

    private String organizationName;
    private String organizationCode;
    private Enum organizationType;
    private String contactEmail;
    private String address;
    private String state;
    private String city;
    private String country;
    private String zipCode;
    private boolean inventory;
    private Enum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
