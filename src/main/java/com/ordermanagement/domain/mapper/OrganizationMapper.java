package com.ordermanagement.domain.mapper;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.requestDTO.OrganizationRequest;
//import com.ordermanagement.domain.responseDTO.CarrierShipperAssociationResponseDto;
//import com.ordermanagement.domain.responseDTO.OrganizationAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.CarrierShipperAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.CarrierShipperWarehouseAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.OrganizationResponse;
//import com.ordermanagement.entity.CarrierShipperAssociation;
import com.ordermanagement.entity.CarrierShipperAssociation;
import com.ordermanagement.entity.Organization;
//import com.ordermanagement.entity.OrganizationAssociation;
import com.ordermanagement.entity.OrganizationAssociation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrganizationMapper {

    public Organization requestToEntity(OrganizationRequest request) {
        Organization organizations = new Organization();
        organizations.setOrganizationName(request.getOrganizationName());
        organizations.setOrganizationCode(request.getOrganizationCode());
        organizations.setOrganizationType(request.getOrganizationType());
        organizations.setContactEmail(request.getContactEmail());
        organizations.setOrganizationContact(request.getOrganizationContact());
        organizations.setAddress(request.getAddress());
        organizations.setCity(request.getCity());
        organizations.setState(request.getState());
        organizations.setZipCode(request.getZipCode());
        organizations.setCountry(request.getCountry());
        organizations.setInventory(request.isInventory());
        organizations.setStatus(Enum.Status.ACTIVE);
        organizations.setCreatedAt(LocalDateTime.now());
        organizations.setUpdatedAt(LocalDateTime.now());
        return organizations;
    }

    public void updateEntityFromRequest(Organization organizations, OrganizationRequest request) {
        organizations.setOrganizationName(request.getOrganizationName());
        organizations.setOrganizationCode(request.getOrganizationCode());
        organizations.setOrganizationType(request.getOrganizationType());
        organizations.setContactEmail(request.getContactEmail());
        organizations.setOrganizationContact(request.getOrganizationContact());
        organizations.setAddress(request.getAddress());
        organizations.setCity(request.getCity());
        organizations.setState(request.getState());
        organizations.setZipCode(request.getZipCode());
        organizations.setCountry(request.getCountry());
        organizations.setInventory(request.isInventory());
        organizations.setStatus(Enum.Status.ACTIVE);
        organizations.setUpdatedAt(LocalDateTime.now());
    }

    public OrganizationResponse entityToResponse(Organization organizations) {
        OrganizationResponse response = new OrganizationResponse();
        response.setOrganizationName(organizations.getOrganizationName());
        response.setOrganizationCode(organizations.getOrganizationCode());
        response.setOrganizationType(organizations.getOrganizationType());
        response.setContactEmail(organizations.getContactEmail());
        response.setAddress(organizations.getAddress());
        response.setState(organizations.getState());
        response.setCity(organizations.getCity());
        response.setCountry(organizations.getCountry());
        response.setZipCode(organizations.getZipCode());
        response.setInventory(organizations.isInventory());
        response.setStatus(organizations.getStatus());
        response.setCreatedAt(organizations.getCreatedAt());
        response.setUpdatedAt(organizations.getUpdatedAt());
        return response;
    }

    public CarrierShipperAssociation convertDtoToCarrierShipperAssociation(Organization carrier, Organization shipperOrganization) {
        CarrierShipperAssociation carrierShipperAssociation = new CarrierShipperAssociation();
        carrierShipperAssociation.setCarrierOrganization(carrier);
        carrierShipperAssociation.setShipperOrganization(shipperOrganization);
        carrierShipperAssociation.setCarrierAssociationStatus(Enum.Status.ACTIVE);
        carrierShipperAssociation.setCreatedAt(LocalDateTime.now());
        carrierShipperAssociation.setUpdatedAt(LocalDateTime.now());
        return carrierShipperAssociation;
    }

    public OrganizationAssociation convertDtoToOrganizationAssociation(Organization carrier, Organization shipper, Organization warehouse) {
        OrganizationAssociation organizationAssociation = new OrganizationAssociation();
        organizationAssociation.setCarrierOrganization(carrier);
        organizationAssociation.setShipperOrganization(shipper);
        organizationAssociation.setWarehouseOrganization(warehouse);
        organizationAssociation.setCarrierShipperWarehouseAssociationStatus(Enum.Status.ACTIVE);
        organizationAssociation.setCreatedAt(LocalDateTime.now());
        organizationAssociation.setUpdatedAt(LocalDateTime.now());
        return organizationAssociation;
    }

    public CarrierShipperAssociationResponseDto convertCarrierShipperAssociationToResponseDto(CarrierShipperAssociation association) {
        CarrierShipperAssociationResponseDto carrierShipperAssociationResponseDto = new CarrierShipperAssociationResponseDto();
        carrierShipperAssociationResponseDto.setShipperId(association.getCarrierOrganization().getId());
        carrierShipperAssociationResponseDto.setCarrierId(association.getShipperOrganization().getId());
        carrierShipperAssociationResponseDto.setAssociation(association.getCarrierAssociationStatus().name());
        return carrierShipperAssociationResponseDto;

    }

    public CarrierShipperWarehouseAssociationResponseDto convertOrganizationAssociationToResponseDto(OrganizationAssociation association) {
        CarrierShipperWarehouseAssociationResponseDto organizationAssociationResponseDto = new CarrierShipperWarehouseAssociationResponseDto();
        organizationAssociationResponseDto.setCarrierId(association.getCarrierOrganization().getId());
        organizationAssociationResponseDto.setShipperId(association.getShipperOrganization().getId());
        organizationAssociationResponseDto.setWarehouseId(association.getWarehouseOrganization().getId());
        organizationAssociationResponseDto.setAssociation(association.getCarrierShipperWarehouseAssociationStatus().name());
        return organizationAssociationResponseDto;
    }
}
