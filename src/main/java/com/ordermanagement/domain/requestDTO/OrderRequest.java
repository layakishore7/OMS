package com.ordermanagement.domain.requestDTO;

import com.ordermanagement.Enum.Enum;
import com.ordermanagement.entity.Customer;
import com.ordermanagement.entity.OrderItem;
import com.ordermanagement.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private Enum.OrderType orderType;

    private Enum.OrderStatus orderStatus;
    // or Enum OrderStatus
    private String originAddress;
    private String carrier;
    private String shipper;
    private String warehouse;
    private Integer customerId;
    private List<OrderItemRequest> orderItems;
}
