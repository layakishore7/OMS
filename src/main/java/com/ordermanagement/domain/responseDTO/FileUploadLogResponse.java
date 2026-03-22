package com.ordermanagement.domain.responseDTO;

import com.ordermanagement.Enum.AppEnums;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileUploadLogResponse {

    private Integer id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private String fileFormat;
    private String shipperOrganization;
    private int successCount;
    private int failedCount;
    private JsonNode successData;
    private JsonNode failedData;
    private LocalDateTime createdAt;
    private AppEnums.FileUploadStatus fileUploadStatus;
}