package com.ordermanagement.domain.responses;

import com.ordermanagement.domain.misc.MetaData;
import com.ordermanagement.domain.responseDTO.FileUploadLogResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadPageResponse {

    private List<FileUploadLogResponse> logResponse;

    private MetaData metaData;
}
