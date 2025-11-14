package com.ordermanagement.repository;

import com.ordermanagement.entity.CarrierShipperAssociation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarrierShipperAssociationRepository extends JpaRepository<CarrierShipperAssociation, Integer> {

    @Query(value = "SELECT csa.* FROM carrier_shipper_association csa WHERE csa.carrier_id = :carrierId AND csa.shipper_id = :shipperId",nativeQuery = true)
    Optional<CarrierShipperAssociation> findByCarrierIdAndShipperId(@Param("carrierId") int carrierId, @Param("shipperId") Integer id);
}
