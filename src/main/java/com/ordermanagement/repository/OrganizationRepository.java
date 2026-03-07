package com.ordermanagement.repository;

import com.ordermanagement.entity.Organization;
import com.ordermanagement.entity.OrganizationAssociation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface OrganizationRepository extends JpaRepository<Organization, Integer> {


    @Query(value = "SELECT * FROM organizations WHERE organization_name = ?1 AND status = 1",nativeQuery = true)
    Optional<Organization> findByOrganizationName(String organizationName);

    @Query(value = "SELECT * FROM organizations WHERE organization_code = ?1 AND status = 1",nativeQuery = true)
    Optional<Organization> findByOrganizationCode(String organizationCode);

    @Query(value = "select * from organizations where status =1",nativeQuery = true)
    Page<Organization> fetchAllOrganizations(String search, PageRequest pageable);

}
