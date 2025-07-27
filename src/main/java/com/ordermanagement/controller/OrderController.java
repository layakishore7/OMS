package com.ordermanagement.controller;

import com.ordermanagement.domain.misc.APIResponse;
import com.ordermanagement.domain.requestDTO.OrderRequest;
import com.ordermanagement.domain.requestDTO.ProductRequest;
import com.ordermanagement.domain.responseDTO.OrderResponse;
import com.ordermanagement.entity.Customer;
import com.ordermanagement.entity.Order;
import com.ordermanagement.entity.Product;
import com.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<APIResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        return APIResponse.created("Order Created Successfully", orderResponse);
    }

}
