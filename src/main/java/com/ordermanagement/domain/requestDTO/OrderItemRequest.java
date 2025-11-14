package com.ordermanagement.domain.requestDTO;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Order;
import com.ordermanagement.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

    public class OrderItemRequest {
        private Integer productId;          // ✅ from "productId": 2
        private Integer pieces;
        private Enum.OrderStatus itemStatus;          // or Enum ItemStatus
        private LocalDateTime shipmentDate;
        private Integer quantity;
        private Double price;
    }

