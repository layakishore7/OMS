package com.ordermanagement.entity;


import com.fasterxml.jackson.databind.JsonNode;
import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "file_upload_log")
@Entity
public class FileUploadLog extends BaseEntity{

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_format")
    private String fileFormat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Organization carrierOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipper_id")
    private Organization shipperOrganization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Organization warehouseOrganization;

    @Column(name = "success_count")
    private int successCount;

    @Column(name = "failed_count")
    private int failedCount;

    @Column(name = "success_data",columnDefinition = "jsonb",nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode successData;


    @Column(name = "failed_data",columnDefinition = "jsonb",nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode failedData;

    @Column(name = "uploaded_by")
    private String uploadedBy;


    @Column(name = "file_upload_status")
    private Enum.fileUploadStatus fileUploadStatus;

}
