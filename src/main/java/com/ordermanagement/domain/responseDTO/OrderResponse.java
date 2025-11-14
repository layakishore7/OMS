package com.ordermanagement.domain.responseDTO;

import com.ordermanagement.Enum.Enum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Integer orderId;

    private com.ordermanagement.Enum.Enum.OrderType orderType;

    private Enum.OrderStatus orderStatus;

    private String originAddress;

    private List<OrderItemResponse> orderItems;

    private String carrier;

    private String shipper;

    private String warehouse;

    private Integer customerId;
}
