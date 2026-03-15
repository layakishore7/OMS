package com.ordermanagement.domain.mapper;
import com.ordermanagement.domain.responseDTO.FileUploadLogResponse;
import com.ordermanagement.entity.FileUploadLog;
import org.springframework.stereotype.Component;

@Component
public class FileUploadLogMapper {


    public FileUploadLogResponse entityToResponse(FileUploadLog fileUploadLog) {

        FileUploadLogResponse fileUploadLogResponse = new FileUploadLogResponse();
        fileUploadLogResponse.setId(fileUploadLog.getId());
        fileUploadLogResponse.setFileName(fileUploadLog.getFileName());
        fileUploadLogResponse.setFileUrl(fileUploadLog.getFileUrl());
        fileUploadLogResponse.setFileFormat(fileUploadLog.getFileFormat());
        fileUploadLogResponse.setFileType(fileUploadLog.getFileType());
        fileUploadLogResponse.setShipperOrganization(fileUploadLog.getShipperOrganization().getOrganizationName());
        fileUploadLogResponse.setSuccessCount(fileUploadLog.getSuccessCount());
        fileUploadLogResponse.setFailedCount(fileUploadLog.getFailedCount());
        fileUploadLogResponse.setSuccessData(fileUploadLog.getSuccessData());
        fileUploadLogResponse.setFailedData(fileUploadLog.getFailedData());
        fileUploadLogResponse.setCreatedAt(fileUploadLog.getCreatedAt());
        return fileUploadLogResponse;
    }
}
