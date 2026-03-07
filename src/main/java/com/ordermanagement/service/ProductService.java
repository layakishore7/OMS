package com.ordermanagement.service;
import com.ordermanagement.Enum.Enum;
import com.ordermanagement.config.AppConfig;
import com.ordermanagement.domain.mapper.ProductMapper;
import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.domain.responses.CategoriesPageResponse;
import com.ordermanagement.domain.responses.ProductsPageResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.Organization;
import com.ordermanagement.entity.Product;
import com.ordermanagement.exceptions.DeletionException;
import com.ordermanagement.exceptions.RecordNotFoundException;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.OrganizationRepository;
import com.ordermanagement.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ProductMapper productMapper;

    private final ModelMapper modelMapper;




    public ProductsPageResponse getAllProducts(String search, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber,pageSize)
                .withSort(Sort.by("product_name").ascending());
        Page<Product> products = productRepository.fetchAllProducts(search,pageable);

        List<ProductResponse> productsResponse = products.stream()
                .map(productMapper::mapToResponse)
                .toList();
        MetaData metaData = new MetaData();
        metaData.setPageNumber(pageNumber);
        metaData.setPageSize(pageSize);
        metaData.setPageCount(products.getTotalPages());
        metaData.setRecordCount(products.getTotalElements());

        return new ProductsPageResponse(productsResponse,metaData);
    }


    public ProductResponse addProduct(ProductRequest request) {

        Organization organization = organizationRepository.findById(request.getShipperId())
                .orElseThrow(() -> new RuntimeException("Shipper Not Found"));

        if (productRepository.existsByProductNameAndShipperId(request.getProductName(),request.getShipperId())) {
            throw new RuntimeException("Product already exists with name: " + request.getProductName());
        }

        Category child = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.requestToEntity(request, child, organization);

        Product savedProduct = productRepository.save(product);

        return productMapper.entityToResponse(savedProduct);
    }


    public ProductResponse getProductById(Integer productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
       return productMapper.entityToResponse(product);
    }


    public ProductResponse updateProduct(Integer productId, ProductRequest request) {

        Organization organization = organizationRepository.findById(request.getShipperId())
                .orElseThrow(() -> new RuntimeException("Shipper Not Found"));

        if (productRepository.existsByProductNameAndShipperIdExcludingProduct(request.getProductName(),request.getShipperId(),productId)) {
            throw new RuntimeException("Product already exists with name: " + request.getProductName());
        }

        Category child = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
       productMapper.updateEntityFromRequest(product,request,organization,child);
        if (request.getCategoryId() != null) {
            Category subCat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(subCat);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.UpdateEntityToResponse(savedProduct);
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new RecordNotFoundException("Product Not Found"));
            product.setStatus(Enum.Status.INACTIVE);
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
    }

}
