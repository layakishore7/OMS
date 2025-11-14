package com.ordermanagement.domain.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String orderType;
    private String orderStatus;
    private String originAddress;
    private String carrier;
    private String shipper;
    private String warehouse;
    private int customer;
    private List<OrderItemRequest> orderItems;

    // Getters and Setters
}
