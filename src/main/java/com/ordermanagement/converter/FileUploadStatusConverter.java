package com.ordermanagement.converter;

import com.ordermanagement.Enum.Enum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FileUploadStatusConverter implements AttributeConverter<Enum.fileUploadStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Enum.fileUploadStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public Enum.fileUploadStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return Enum.fileUploadStatus.fromValues(dbData);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
