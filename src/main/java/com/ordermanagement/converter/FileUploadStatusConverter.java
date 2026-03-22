package com.ordermanagement.converter;

import com.ordermanagement.Enum.AppEnums;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FileUploadStatusConverter implements AttributeConverter<AppEnums.FileUploadStatus, String> {

    @Override
    public String convertToDatabaseColumn(AppEnums.FileUploadStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return String.valueOf(attribute.getValue());
    }

    @Override
    public AppEnums.FileUploadStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            // Try parsing as integer first (e.g., "0", "1", "3")
            int value = Integer.parseInt(dbData);
            return AppEnums.FileUploadStatus.fromValues(value);
        } catch (NumberFormatException e) {
            // If not an integer, try parsing as the enum name (e.g., "COMPLETED", "PROCESSING")
            try {
                return AppEnums.FileUploadStatus.valueOf(dbData.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }
    }
}
