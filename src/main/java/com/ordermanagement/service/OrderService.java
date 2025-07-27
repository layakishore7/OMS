package com.ordermanagement.service;

import com.ordermanagement.domain.mapper.OrderMapper;
import com.ordermanagement.domain.requestDTO.OrderItemRequest;
import com.ordermanagement.domain.requestDTO.OrderRequest;
import com.ordermanagement.domain.responseDTO.OrderResponse;
import com.ordermanagement.entity.Customer;
import com.ordermanagement.entity.Order;
import com.ordermanagement.entity.Product;
import com.ordermanagement.repository.CustomerRepository;
import com.ordermanagement.repository.OrderRepository;
import com.ordermanagement.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;
    public OrderResponse createOrder(OrderRequest orderRequest) {

        // 1. Fetch Customer
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + orderRequest.getCustomerId()));

        // 2. Extract and Validate Product IDs
        List<Integer> productIds = orderRequest.getOrderItems().stream()
                .map(item -> {
                    if (item.getProductId() == null) {
                        throw new IllegalArgumentException("Product or product ID is missing in order item.");
                    }
                    return item.getProductId();
                })
                .collect(Collectors.toList());

        // 3. Fetch Products
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new RuntimeException("Some products not found for IDs: " + productIds);
        }

        // 4. Map to Entity
        Order createdOrder = orderMapper.requestToEntity(orderRequest, customer, products);

        // 5. Save
        Order savedOrder = orderRepository.save(createdOrder);

        // 6. Map to DTO
        return orderMapper.entityToResponse(savedOrder);
    }


}
