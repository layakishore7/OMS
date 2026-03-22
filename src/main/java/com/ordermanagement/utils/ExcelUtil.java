package com.ordermanagement.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ordermanagement.Enum.Enum;
import com.ordermanagement.domain.mapper.ProductMapper;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.entity.Category;
import com.ordermanagement.entity.FileUploadLog;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CategoryRepository;
import com.ordermanagement.repository.FileUploadLogRepository;
import com.ordermanagement.repository.ProductRepository;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ExcelUtil {

    @Autowired
    private static CategoryRepository categoryRepository;

    @Autowired
    private static ProductMapper productMapper;

    @Autowired
    private static ProductRepository productRepository;

    @Autowired
    private static FileUploadLogRepository fileUploadLogRepository;

    private static final String SHEET_NAME = "Products";

    private static final String[] HEADERS = {
            "PRODUCT_NAME",
            "PRODUCT_ID",
            "DESCRIPTION",
            "CATEGORY",
            "LENGTH",
            "BREADTH",
            "HEIGHT",
            "DIMENSION_UOM",
            "WEIGHT",
            "WEIGHT_UOM",
            "SERIALIZABLE",
            "AVAILABLE_QUANTITY"
    };

    public static ByteArrayInputStream productsToExcel(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(SHEET_NAME);

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            int rowIdx = 1;

            for (Product product : products) {

                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(
                        product.getProductName() != null ? product.getProductName() : "");

                row.createCell(1).setCellValue(
                        product.getProductUniqueId() != null ? product.getProductUniqueId() : "");

                row.createCell(2).setCellValue(
                        product.getDescription() != null ? product.getDescription() : "");

                row.createCell(3).setCellValue(
                        product.getCategory() != null ? product.getCategory().getCategoryName() : "");

                // ✅ FIXED NULL HANDLING FOR NUMBERS
                row.createCell(4).setCellValue(
                        product.getLength() != null ? product.getLength() : 0.0);

                row.createCell(5).setCellValue(
                        product.getBreadth() != null ? product.getBreadth() : 0.0);

                row.createCell(6).setCellValue(
                        product.getHeight() != null ? product.getHeight() : 0.0);

                row.createCell(7).setCellValue(
                        product.getDimensionUom() != null ? product.getDimensionUom() : "");

                row.createCell(8).setCellValue(
                        product.getWeight() != null ? product.getWeight() : 0.0);

                row.createCell(9).setCellValue(
                        product.getWeightUom() != null ? product.getWeightUom() : "");

                row.createCell(10).setCellValue(
                        product.getSerializable() != null ? product.getSerializable() : false);

                row.createCell(11).setCellValue(
                        product.getAvailableQuantity() != null ? product.getAvailableQuantity() : 0);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);

            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException("Failed to Export the Excel File", e);
        }
    }

    public static List<ProductRequest> excelToProducts(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rows = sheet.iterator();

            List<ProductRequest> products = new ArrayList<>();

            rows.next();

            while (rows.hasNext()) {

                Row row = rows.next();

                ProductRequest product = new ProductRequest();

                DataFormatter formatter = new DataFormatter();

                product.setProductName(formatter.formatCellValue(row.getCell(0)));

                product.setProductUniqueId(formatter.formatCellValue(row.getCell(1)));

                product.setDescription(formatter.formatCellValue(row.getCell(2)));

                product.setCategoryName(formatter.formatCellValue(row.getCell(3)));

                String length = formatter.formatCellValue(row.getCell(4));
                if (!length.isBlank()) {
                    product.setLength(Double.valueOf(length));
                }

                String breadth = formatter.formatCellValue(row.getCell(5));
                if (!breadth.isBlank()) {
                    product.setBreadth(Double.valueOf(breadth));
                }

                String height = formatter.formatCellValue(row.getCell(6));
                if (!height.isBlank()) {
                    product.setHeight(Double.valueOf(height));
                }

                product.setDimensionUom(formatter.formatCellValue(row.getCell(7)));

                String weight = formatter.formatCellValue(row.getCell(8));
                if (!weight.isBlank()) {
                    product.setWeight(Double.valueOf(weight));
                }

                product.setWeightUom(formatter.formatCellValue(row.getCell(9)));

                String serializable = formatter.formatCellValue(row.getCell(10));
                if (!serializable.isBlank()) {
                    product.setSerializable(Boolean.valueOf(serializable));
                }

                products.add(product);
            }

            workbook.close();
            return products;

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file");
        }
    }


}
