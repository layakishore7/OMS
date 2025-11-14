package com.ordermanagement.repository;

import com.ordermanagement.entity.OrganizationAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrganizationAssociationRepository extends JpaRepository<OrganizationAssociation, Integer> {

    @Query(value = "SELECT oa.* FROM organization_association oa WHERE oa.carrier_id = :carrierId AND oa.shipper_id = :shipperId", nativeQuery = true)
    List<OrganizationAssociation> fetchByCarrierAndShipper(
            @Param("carrierId") Integer carrierId,
            @Param("shipperId") Integer shipperId
    );

    @Query(value = "SELECT oa.* FROM organization_association oa WHERE oa.carrier_id = :carrierId AND oa.shipper_id = :shipperId AND oa.warehouse_id = :warehouseId", nativeQuery = true)
    List<OrganizationAssociation> fetchByCarrierAndShipperAndWarehouse(
            @Param("carrierId") Integer carrierId,
            @Param("shipperId") Integer shipperId,
            @Param("warehouseId") Integer warehouseId
    );
}
