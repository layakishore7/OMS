package com.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.requestDTO.ProductsBulkDeleteDto;
import com.ordermanagement.domain.responseDTO.FileUploadLogResponse;
import com.ordermanagement.domain.responseDTO.ProductResponse;
import com.ordermanagement.domain.responses.FileUploadPageResponse;
import com.ordermanagement.domain.responses.ProductsPageResponse;
import com.ordermanagement.entity.FileUploadLog;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.FileUploadLogRepository;
import com.ordermanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    FileUploadLogRepository fileUploadLogRepository;


    @GetMapping("/products")
    public ResponseEntity<APIResponse> getAllProducts(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        try {
            ProductsPageResponse products = productService.getAllProducts(search, pageNumber, size);
            return APIResponse.success(products);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/products/shipper/{shipperId}")
    public ResponseEntity<APIResponse> getAllProductsByShipper(
            @PathVariable("shipperId") Integer shipperId) {

        try {
            List<ProductResponse> products = productService.getAllProductsByShipperId(shipperId);
            return APIResponse.success(products);
        } catch (Exception e) {
            return APIResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ProductRequest request = mapper.readValue(productJson, ProductRequest.class);

        ProductResponse response = productService.addProduct(request, image);

        return APIResponse.created("Product Created Successfully", response);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<APIResponse> getProductById(@PathVariable("productId") Integer productId) {
        ProductResponse response = productService.getProductById(productId);
        return APIResponse.success(response);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<APIResponse> updateProductById(@PathVariable("productId") Integer productId,
                                                         @RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productService.updateProduct(productId, product);
        return APIResponse.updated("Product Updated Successfully", updatedProduct);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<APIResponse> deleteProductById(@PathVariable("productId") Integer productId) {
        productService.deleteProduct(productId);
        return APIResponse.success("Product Deleted Successfully");
    }

    @DeleteMapping("/products/bulk-delete")
    public ResponseEntity<APIResponse> deleteProducts(@RequestParam(name = "orgId") Integer orgId,
                                                      @RequestBody ProductsBulkDeleteDto productIds) {
        productService.deleteProducts(orgId, productIds);
        return APIResponse.success("Products Deleted Successfully");
    }

    @GetMapping("products/export")
    public ResponseEntity<InputStreamResource> exportProducts() {

        ByteArrayInputStream stream = productService.exportProducts();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=products.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(stream));
    }

    @PostMapping(value = "/products/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<APIResponse> importProducts(
            @RequestParam("file") MultipartFile file,
            @RequestParam("organizationId") Integer organizationId) {

        productService.importProducts(file, organizationId);

        return APIResponse.success("Products Imported Successfully");

    }

    @PostMapping(value = "/products/import/async", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> AsyncImportProducts(
            @RequestParam("file") MultipartFile file,
            @RequestParam("organizationId") Integer organizationId) {

        Integer logId = productService.startImport(file, organizationId);

        return ResponseEntity.ok(Map.of(
                "message", "File Upload Started",
                "logId", logId
        ));
    }

    @GetMapping("/products/file-logs/{shipperId}")
    public ResponseEntity<APIResponse> getFileLogs(
            @PathVariable Integer shipperId,
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "size", defaultValue = "5") int size) {

        try {

            FileUploadPageResponse response =
                    productService.getImportFileLogs(search, pageNumber, size, shipperId);

            return APIResponse.success(response);

        } catch (Exception e) {

            return APIResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
