package com.ordermanagement.service;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.mapper.OrganizationMapper;
import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.requestDTO.*;
import com.ordermanagement.domain.responseDTO.CarrierShipperAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.CarrierShipperWarehouseAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.OrganizationResponse;
import com.ordermanagement.domain.responses.OrganizationPageResponse;
import com.ordermanagement.entity.*;
import com.ordermanagement.repository.CarrierShipperAssociationRepository;
import com.ordermanagement.repository.OrganizationAssociationRepository;
import com.ordermanagement.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {


    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final CarrierShipperAssociationRepository carrierShipperAssociationRepository;

    private final OrganizationAssociationRepository organizationAssociationRepository;

    public OrganizationResponse createOrganization(OrganizationRequest organizationRequest) {
        // Check if organization with same name already exists
        if (organizationRepository.findByOrganizationName(organizationRequest.getOrganizationName()).isPresent()) {
            throw new RuntimeException("Organization with name " + organizationRequest.getOrganizationName() + " already exists");
        }

        if (organizationRepository.findByOrganizationCode(organizationRequest.getOrganizationCode()).isPresent()) {
            throw new RuntimeException("Organization with code " + organizationRequest.getOrganizationCode() + " already exists");
        }
        Organization organization = organizationMapper.requestToEntity(organizationRequest);
        organization = organizationRepository.save(organization);
        return organizationMapper.entityToResponse(organization);
    }

    public OrganizationPageResponse getAllOrganizations(String search, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber,pageSize)
                .withSort(Sort.by("organization_name").ascending());
        Page<Organization> organizations = organizationRepository.fetchAllOrganizations(search,pageable);

        List<Organization> organizationsResponse = organizations.stream().toList();

        MetaData metaData = new MetaData();
        metaData.setPageNumber(pageNumber);
        metaData.setPageSize(pageSize);
        metaData.setPageCount(organizations.getTotalPages());
        metaData.setRecordCount(organizations.getTotalElements());

        return new OrganizationPageResponse(organizationsResponse,metaData);
    }

    public OrganizationResponse updateOrganization(Integer organizationId, OrganizationRequest request) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        organizationMapper.updateEntityFromRequest(organization, request);
        Organization savedOrganization = organizationRepository.save(organization);
        return organizationMapper.entityToResponse(savedOrganization);
    }

    public void deleteOrganization(Integer organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        organization.setStatus(Enum.Status.INACTIVE);
        organization.setUpdatedAt(LocalDateTime.now());
        organizationRepository.save(organization);
    }

    public Organization checkOrganizationExistsById(int id){
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found "+id));
    }

    public List<CarrierShipperAssociationResponseDto> linkShipperToCarrier(
            int carrierId, CarrierShipperAssociationDto carrierShipperAssociationDto) {

        Organization carrier = organizationRepository.findById(carrierId)
                .orElseThrow(() -> new RuntimeException("Carrier not found"));

        List<CarrierShipperAssociation> associationsToSave = new ArrayList<>();

        for (CarrierShipperAssociationLinkDto shipperDto : carrierShipperAssociationDto.getOrganizations()) {
            Organization shipper = checkOrganizationExistsById(shipperDto.getShipperId());
            Enum.Status status = Enum.Status.valueOf(shipperDto.getAssociation());

            CarrierShipperAssociation carrierShipperAssociation = carrierShipperAssociationRepository
                    .findByCarrierIdAndShipperId(carrierId, shipper.getId())
                    .orElseGet(() -> organizationMapper.convertDtoToCarrierShipperAssociation(carrier, shipper));

            carrierShipperAssociation.setCarrierAssociationStatus(status);

            associationsToSave.add(carrierShipperAssociation);

            updateOrganizationAssociationStatusForShipper(carrier.getId(), shipper.getId(), status);
        }

        carrierShipperAssociationRepository.saveAll(associationsToSave);

        return associationsToSave.stream()
                .map(organizationMapper::convertCarrierShipperAssociationToResponseDto)
                .collect(Collectors.toList());
    }

    private void updateOrganizationAssociationStatusForShipper(int carrierId, int shipperId, Enum.Status status) {
        List<OrganizationAssociation> associations =
                organizationAssociationRepository.fetchByCarrierAndShipper(carrierId, shipperId);

        for (OrganizationAssociation association : associations) {
            association.setCarrierShipperWarehouseAssociationStatus(status);
        }
        organizationAssociationRepository.saveAll(associations);
    }

    public List<CarrierShipperWarehouseAssociationResponseDto> linkWarehouse(
            int carrierId,
            CarrierShipperWarehouseAssociationDto carrierShipperWarehouseAssociationDto) {

        Organization carrier = organizationRepository.findById(carrierId)
                .orElseThrow(() -> new RuntimeException("Carrier not found"));

        List<OrganizationAssociation> warehouseAssociationsToSave = new ArrayList<>();

        for (CarrierShipperWarehouseAssociationLinkDto warehouseDto :
                carrierShipperWarehouseAssociationDto.getOrganizations()) {

            Organization shipper = checkOrganizationExistsById(warehouseDto.getShipperId());
            Organization warehouse = checkOrganizationExistsById(warehouseDto.getWarehouseId());
            Enum.Status status = Enum.Status.valueOf(warehouseDto.getAssociation());

            Optional<OrganizationAssociation> existingAssociation =
                    organizationAssociationRepository.fetchByCarrierAndShipperAndWarehouse(
                                    carrierId, shipper.getId(), warehouse.getId())
                            .stream().findFirst();

            OrganizationAssociation warehouseAssociation = existingAssociation
                    .orElseGet(() -> organizationMapper.convertDtoToOrganizationAssociation(carrier, shipper, warehouse));

            warehouseAssociation.setCarrierShipperWarehouseAssociationStatus(status);

            warehouseAssociationsToSave.add(warehouseAssociation);

            updateOrganizationAssociationStatusForWarehouse(carrierId, shipper.getId(), warehouse.getId(), status);
        }

        organizationAssociationRepository.saveAll(warehouseAssociationsToSave);

        return warehouseAssociationsToSave.stream()
                .map(organizationMapper::convertOrganizationAssociationToResponseDto)
                .collect(Collectors.toList());
    }


    private void updateOrganizationAssociationStatusForWarehouse(int carrierId, int shipperId, int warehouseId, Enum.Status status) {
        List<OrganizationAssociation> associations =
                organizationAssociationRepository.fetchByCarrierAndShipperAndWarehouse
                        (carrierId, shipperId,warehouseId);

        for (OrganizationAssociation association : associations) {
            association.setCarrierShipperWarehouseAssociationStatus(status);
        }
        organizationAssociationRepository.saveAll(associations);
    }

}
