package com.ordermanagement.domain.responseDTO;

import com.ordermanagement.Enum.Enum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Integer id;

    private String categoryName;

    private String description;

    private Enum.Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
