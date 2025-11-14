package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.responseDTO.OrganizationResponse;
import com.ordermanagement.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationPageResponse {

    private List<Organization> organizations;

    private MetaData metaData;
}
