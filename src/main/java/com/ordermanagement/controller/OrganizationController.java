package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.*;
import com.ordermanagement.domain.responseDTO.CarrierShipperAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.CarrierShipperWarehouseAssociationResponseDto;
import com.ordermanagement.domain.responseDTO.OrganizationResponse;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.domain.responses.OrganizationPageResponse;
import com.ordermanagement.entity.CarrierShipperAssociation;
import com.ordermanagement.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrganizationController {


    private final OrganizationService organizationService;

    @GetMapping("/organizations")
    public ResponseEntity<APIResponse> getAllProducts(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int  size){

        try {
            OrganizationPageResponse organizations = organizationService.getAllOrganizations(search,pageNumber,size);
            return APIResponse.success(organizations);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @PostMapping("/create-organization")
    public ResponseEntity<APIResponse> createOrganization(@RequestBody OrganizationRequest organizationRequest) {
        OrganizationResponse organizationResponse = organizationService.createOrganization(organizationRequest);
        return APIResponse.created("Organization Created Successfully", organizationResponse);
    }

    @PutMapping("/update-organization/{organizationId}")
    public ResponseEntity<APIResponse> updateOrganizationById(@PathVariable Integer organizationId,@RequestBody OrganizationRequest organization) {
        OrganizationResponse updatedOrganization = organizationService.updateOrganization(organizationId,organization);
        return APIResponse.updated("Organization Updated Successfully",updatedOrganization);
    }

    @DeleteMapping("/delete-organization/{organizationId}")
    public ResponseEntity<APIResponse> deleteOrganizationById(@PathVariable Integer organizationId) {
        organizationService.deleteOrganization(organizationId);
        return APIResponse.success("Organization Deleted Successfully");
    }

    @PostMapping("/link-shipper")
    public ResponseEntity<APIResponse> linkShipperToCarrierAssociation(@RequestParam(name = "carrierId")Integer carrierId,@RequestBody CarrierShipperAssociationDto associationLinkDto) {
        List<CarrierShipperAssociationResponseDto> associationResponseDto = organizationService.linkShipperToCarrier(carrierId,associationLinkDto);
        return APIResponse.created("Association Created Successfully", associationResponseDto);
    }

    @PostMapping("/link-warehouse")
    public ResponseEntity<APIResponse> linkWarehouseToCarrierAssociation(@RequestParam(name = "carrierId")Integer carrierId,@RequestBody CarrierShipperWarehouseAssociationDto associationLinkDto) {
        List<CarrierShipperWarehouseAssociationResponseDto> associationResponseDto = organizationService.linkWarehouse(carrierId,associationLinkDto);
        return APIResponse.created("Association Created Successfully", associationResponseDto);
    }








}
