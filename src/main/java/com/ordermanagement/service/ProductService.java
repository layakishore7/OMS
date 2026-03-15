package com.ordermanagement.service;

import com.cloudinary.Cloudinary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.mapper.FileUploadLogMapper;
import com.ordermanagement.domain.mapper.ProductMapper;
import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.requestDTO.ProductsBulkDeleteDto;
import com.ordermanagement.domain.requestDTO.ProductIdsBulkDto;
import com.ordermanagement.domain.responseDTO.FileUploadLogResponse;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.domain.responses.FileUploadPageResponse;
import com.ordermanagement.domain.responses.ProductsPageResponse;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.FileUploadLog;
import com.ordermanagement.entity.Organization;
import com.ordermanagement.entity.Product;
import com.ordermanagement.exceptions.RecordNotFoundException;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.FileUploadLogRepository;
import com.ordermanagement.repository.OrganizationRepository;
import com.ordermanagement.repository.ProductRepository;
import com.ordermanagement.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    FileUploadLogRepository fileUploadLogRepository;

    @Autowired
    FileUploadLogMapper fileUploadLogMapper;

    @Autowired
    private ImageService imageService;


    public ProductsPageResponse getAllProducts(String search, int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize)
                .withSort(Sort.by("product_name").ascending());
        Page<Product> products = productRepository.fetchAllProducts(search, pageable);

        List<ProductResponse> productsResponse = products.stream()
                .map(productMapper::mapToResponse)
                .toList();
        MetaData metaData = new MetaData();
        metaData.setPageNumber(pageNumber);
        metaData.setPageSize(pageSize);
        metaData.setPageCount(products.getTotalPages());
        metaData.setRecordCount(products.getTotalElements());

        return new ProductsPageResponse(productsResponse, metaData);
    }

    public ProductResponse addProduct(ProductRequest request, MultipartFile image) {

        Organization organization = organizationRepository.findById(request.getShipperId())
                .orElseThrow(() -> new RuntimeException("Shipper Not Found"));

        if (productRepository.existsByProductNameAndShipperId(request.getProductName(), request.getShipperId())) {
            throw new RuntimeException("Product already exists with name: " + request.getProductName());
        }

        Category child = categoryRepository.findByCategoryNameAndStatus(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productMapper.requestToEntity(request, request.getShipperId());

        if (image!= null && !image.isEmpty()) {
            String imageUrl = imageService.uploadImage(image);
            product.setUploadImage(imageUrl);
        }

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

        if (productRepository.existsByProductNameAndShipperIdExcludingProduct(request.getProductName(),
                request.getShipperId(), productId)) {
            throw new RuntimeException("Product already exists with name: " + request.getProductName());
        }

        Category child = categoryRepository.findByCategoryNameAndStatus(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productMapper.updateEntityFromRequest(product, request, organization, child);
        if (request.getCategoryName() != null) {
            Category subCat = categoryRepository.findByCategoryNameAndStatus(request.getCategoryName())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(subCat);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.UpdateEntityToResponse(savedProduct);
    }

    public void deleteProduct(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RecordNotFoundException("Product Not Found"));
        product.setStatus(Enum.Status.INACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void deleteProducts(Integer orgId, ProductsBulkDeleteDto productIds) {
        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization Not Found"));

        if (productIds == null || productIds.getProductIds() == null || productIds.getProductIds().isEmpty()) {
            throw new IllegalArgumentException("Product IDs must be provided");
        }
        for (ProductIdsBulkDto link : productIds.getProductIds()) {
            Integer productId = link.getProductId();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RecordNotFoundException("Product Not Found with id: " + productId));
            if (!product.getOrganization().getId().equals(orgId)) {
                throw new RuntimeException("Product id " + productId + " does not belong to organization " + orgId);
            }
            product.setStatus(Enum.Status.INACTIVE);

            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
        }
    }

    public List<ProductResponse> getAllProductsByShipperId(int shipperId) {

       List<Product> products =  productRepository.fetchProductsByShipperId(shipperId);

        return products.stream()
                 .map(productMapper::mapToResponse)
                 .toList();

    }

    public ByteArrayInputStream exportProducts() {
        List<Product> products = productRepository.findAll();
        return ExcelUtil.productsToExcel(products);
    }

    public void importProducts(MultipartFile file,Integer organizationId) {
        try {
            List<ProductRequest> requests =
                    ExcelUtil.excelToProducts(file.getInputStream());

            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(()-> new RuntimeException("Organization not found"));

            List<Product> products = new ArrayList<>();

            for(ProductRequest request : requests) {
                Product product = new Product();

                product.setProductName(request.getProductName());

                if (request.getProductUniqueId() != null && !request.getProductUniqueId().isBlank()) {
                    product.setProductUniqueId(request.getProductUniqueId());
                } else {
                    String uniqueId = organization.getOrganizationCode() + "-"
                            + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                    product.setProductUniqueId(uniqueId);
                }
                product.setDescription(request.getDescription());



                Category child = categoryRepository.findByCategoryNameAndStatus(request.getCategoryName())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                product.setCategory(child);
                product.setOrganization(organization);
                product.setLength(request.getLength());
                product.setBreadth(request.getBreadth());
                product.setHeight(request.getHeight());
                product.setDimensionUom(request.getDimensionUom());
                product.setWeight(request.getWeight());
                product.setWeightUom(request.getWeightUom());
                product.setSerializable(request.getSerializable());
                product.setAvailableQuantity(0);
                products.add(product);
            }

            productRepository.saveAll(products);

        } catch (IOException e) {
            throw new RuntimeException("Import Failed");
        }
    }

    @Async("importExecutor")
    public void processImport(Integer id, MultipartFile file, Integer organizationId) {

        ObjectMapper mapper = new ObjectMapper();

        ArrayNode successArray = mapper.createArrayNode();
        ArrayNode failedArray = mapper.createArrayNode();

        int success = 0;
        int failed = 0;
        int rowNumber = 2;

        try {
            FileUploadLog log = fileUploadLogRepository.findById(id).orElseThrow();

            List<ProductRequest> rows = ExcelUtil.excelToProducts(file.getInputStream());

            for (ProductRequest request : rows) {
                try {

                    if (productRepository.existsByProductUniqueIdAndShipperId(request.getProductUniqueId(),organizationId)) {
                        throw new RuntimeException(
                                "Product unique id already exists: " + request.getProductUniqueId());
                    }

                    if (productRepository.existsByProductNameAndShipperId(request.getProductName(),organizationId)) {
                        throw new RuntimeException(
                                "Product name already exists: " + request.getProductName());
                    }
                    Product product = productMapper.requestToEntity(request,organizationId);
                    productRepository.save(product);

                    ObjectNode successNode = mapper.createObjectNode();
                    successNode.put("id",product.getId());
                    successNode.put("productName",product.getProductName());
                    successNode.put("productUniqueId",product.getProductUniqueId());
                    successArray.add(successNode);
                    success++;
                } catch (Exception e) {

                    ObjectNode failedNode = mapper.createObjectNode();
                    failedNode.put("rowNumber",rowNumber);
                    failedNode.put("productName",request.getProductName());
                    failedNode.put("errorMessage",e.getMessage());
                    failedArray.add(failedNode);
                    failed++;
                }
                rowNumber++;
            }
            log.setSuccessCount(success);
            log.setFailedCount(failed);
            log.setSuccessData(successArray);
            log.setFailedData(failedArray);
            log.setFileUploadStatus(Enum.fileUploadStatus.COMPLETED);
            fileUploadLogRepository.save(log);

        } catch (Exception e) {
            FileUploadLog fileUploadLog = fileUploadLogRepository.findById(id).orElseThrow();
            fileUploadLog.setFileUploadStatus(Enum.fileUploadStatus.FAILED);
            fileUploadLogRepository.save(fileUploadLog);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {

        String uploadDir = "uploads/";

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path path = Paths.get(uploadDir + fileName);

        Files.createDirectories(path.getParent());

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "http://localhost:8080/uploads/" + fileName;
    }



    public Integer startImport(MultipartFile file, Integer organizationId) {

        FileUploadLog log = new FileUploadLog();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode emptyArray = mapper.createArrayNode();
        log.setFileName(file.getOriginalFilename());
        log.setFileType("Products");
        log.setFileFormat("excel");
        try {
            log.setFileUrl(uploadFile(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Organization organization = organizationRepository.findById(organizationId).orElseThrow();
        log.setShipperOrganization(organization);
        log.setSuccessCount(0);
        log.setFailedCount(0);
        log.setUploadedBy("USER");
        log.setSuccessData(emptyArray);   // important
        log.setFailedData(emptyArray);
        log.setFileUploadStatus(Enum.fileUploadStatus.PROCESSING);

        log = fileUploadLogRepository.save(log);

        processImport(log.getId(),file,organizationId);

        return log.getId();
    }


    public FileUploadPageResponse getImportFileLogs(String search, int pageNumber, int pageSize, Integer shipperId) {

        PageRequest pageable = PageRequest.of(pageNumber,pageSize)
                .withSort(Sort.by("id").descending());
        Organization organization = organizationRepository.findById(shipperId).orElseThrow();
        Page<FileUploadLog> uploadLog = fileUploadLogRepository.findByShipperIdAndSearch(shipperId,search,pageable);

        List<FileUploadLogResponse> uploadLogResponse =  uploadLog.stream()
                .map(fileUploadLogMapper::entityToResponse)
                .toList();
        MetaData metaData = new MetaData();
        metaData.setPageNumber(pageNumber);
        metaData.setPageSize(pageSize);
        metaData.setPageCount(uploadLog.getTotalPages());
        metaData.setRecordCount(uploadLog.getTotalElements());

        return new FileUploadPageResponse(uploadLogResponse,metaData);
    }
}
