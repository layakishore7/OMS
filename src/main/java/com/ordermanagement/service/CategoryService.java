package com.ordermanagement.service;

import com.ordermanagement.domain.mapper.CategoryMapper;
import com.ordermanagement.domain.requestDTO.CategoryRequest;
import com.ordermanagement.domain.responseDTO.CategoryResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Organization;
import com.ordermanagement.exceptions.RecordAlreadyExistsException;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private OrganizationRepository organizationRepository;



    @Transactional
    public List<CategoryResponse> addProductCategory(CategoryRequest request) {

        // Load organization (shipper)
        Organization shipperOrganization = organizationRepository.findById(request.getShipperId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found for id: " + request.getShipperId()));

        // Basic validation of request combinators
        validateProductCategoryRequest(request);

        List<CategoryResponse> responses = new ArrayList<>();

        try {
            // CASE 1: parentCategoryId provided -> use existing parent and create child
            if (request.getParentCategoryId() != null) {
                // child name is required in this flow
                if (request.getCategoryName() == null || request.getCategoryName().trim().isEmpty()) {
                    throw new IllegalArgumentException("Subcategory name is required when parentCategoryId is provided.");
                }

                Category parent = categoryRepository.findById(request.getParentCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException("Parent Category not found for id: " + request.getParentCategoryId()));

                // verify parent belongs to the organization
                if (parent.getShipperOrganization() == null
                        || parent.getShipperOrganization().getId() == null
                        || !parent.getShipperOrganization().getId().equals(shipperOrganization.getId())) {
                    throw new IllegalArgumentException("Parent category does not belong to the provided organization");
                }

                // ensure no duplicate child under this parent
                checkCategoryExists(request.getCategoryName().trim(), shipperOrganization, parent);

                Category child = createAndSaveCategory(request, request.getCategoryName().trim(), shipperOrganization, parent);
                responses.add(categoryMapper.convertEntityToCategoryResponse(child));
                return responses;
            }

            // CASE 2: parentCategoryId is null, but parentCategoryName provided
            if (request.getParentCategoryName() != null && !request.getParentCategoryName().trim().isEmpty()) {

                // If client only provided parentCategoryName (no categoryName) -> create parent only
                if (request.getCategoryName() == null || request.getCategoryName().trim().isEmpty()) {
                    checkCategoryExists(request.getParentCategoryName().trim(), shipperOrganization, null);
                    Category parent = createAndSaveCategory(request, request.getParentCategoryName().trim(), shipperOrganization, null);
                    responses.add(categoryMapper.convertEntityToCategoryResponse(parent));
                    return responses;
                }

                // Both parent and child names provided -> create both
                checkCategoryExists(request.getParentCategoryName().trim(), shipperOrganization, null);
                Category parent = createAndSaveCategory(request, request.getParentCategoryName().trim(), shipperOrganization, null);

                checkCategoryExists(request.getCategoryName().trim(), shipperOrganization, parent);
                Category child = createAndSaveCategory(request, request.getCategoryName().trim(), shipperOrganization, parent);

                responses.add(categoryMapper.convertEntityToCategoryResponse(parent));
                responses.add(categoryMapper.convertEntityToCategoryResponse(child));
                return responses;
            }

            // If we reach here, request was invalid (should have been captured earlier)
            throw new IllegalArgumentException("Invalid request combination for categories");

        } catch (DataIntegrityViolationException dive) {
            // This will catch unique constraint violations caused by race conditions
            throw new RecordAlreadyExistsException("Category already exists (concurrent create or unique constraint).");
        }
    }



    private Category createAndSaveCategory(CategoryRequest request, String categoryName, Organization shipperOrganization, Category parentCategory) {
        // Build entity via mapper; ensure we supply the chosen categoryName (mapper should accept it)
        Category category = categoryMapper.convertCategoryRequestToEntity(request, categoryName, shipperOrganization);
        category.setParentCategory(parentCategory);
        return categoryRepository.save(category);
    }

    private void checkCategoryExists(String categoryName, Organization shipperOrganization, Category parentCategory) {
        if (categoryName == null) return;

        Optional<Category> existing;
        if (parentCategory == null) {
            existing = categoryRepository.findByCategoryNameAndShipperOrganization(categoryName, shipperOrganization);
        } else {
            existing = categoryRepository.findByCategoryNameAndShipperOrganizationAndParentCategory(categoryName, shipperOrganization, parentCategory);
        }
        if (existing.isPresent()) {
            throw new RecordAlreadyExistsException("Category Already Exists: " + categoryName);
        }
    }

    private void validateProductCategoryRequest(CategoryRequest request) {
        // require shipperId always
        if (request.getShipperId() == null) {
            throw new IllegalArgumentException("shipperId is required");
        }
        // require at least one parent identifier (id or name)
        if (request.getParentCategoryId() == null && (request.getParentCategoryName() == null || request.getParentCategoryName().trim().isEmpty())) {
            throw new IllegalArgumentException("Either parentCategoryId or parentCategoryName is required");
        }
        // When parentCategoryId is provided, child name must be present
        if (request.getParentCategoryId() != null && (request.getCategoryName() == null || request.getCategoryName().trim().isEmpty())) {
            throw new IllegalArgumentException("Subcategory Name is required when parentCategoryId is provided");
        }
    }
}
