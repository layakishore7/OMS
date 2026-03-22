package com.ordermanagement.repository;

import com.ordermanagement.entity.FileUploadLog;
import com.ordermanagement.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileUploadLogRepository extends JpaRepository<FileUploadLog,Integer> {


    @Query(value = """
        SELECT * FROM file_upload_log
        WHERE shipper_id = :shipperId
        AND LOWER(file_name) LIKE LOWER(CONCAT('%',:search,'%'))
        """,
            countQuery = """
        SELECT COUNT(*) FROM file_upload_log
        WHERE shipper_id = :shipperId
        AND LOWER(file_name) LIKE LOWER(CONCAT('%',:search,'%'))
        """,
            nativeQuery = true)
    Page<FileUploadLog> findByShipperIdAndSearch(
            @Param("shipperId") Integer shipperId,
            @Param("search") String search,
            Pageable pageable
    );

}
