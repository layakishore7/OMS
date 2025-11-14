package com.ordermanagement.domain.mapper;

import com.ordermanagement.domain.requestDTO.OrderItemRequest;
import com.ordermanagement.domain.requestDTO.OrderRequest;
import com.ordermanagement.domain.responseDTO.OrderItemResponse;
import com.ordermanagement.domain.responseDTO.OrderResponse;
import com.ordermanagement.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public Order requestToEntity(OrderRequest orderRequest, Customer customer, List<Product> products) {
        Order order = new Order();

        order.setOrderType(orderRequest.getOrderType());
        order.setStatus(orderRequest.getOrderStatus());
        order.setOriginAddress(orderRequest.getOriginAddress());
        order.setCarrier(orderRequest.getCarrier());
        order.setShipper(orderRequest.getShipper());
        order.setWarehouse(orderRequest.getWarehouse());
        order.setCustomer(customer);

        // Map each order item
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(itemReq -> {
            OrderItem orderItem = new OrderItem();

            Product matchedProduct = products.stream()
                    .filter(p -> p.getId().equals(itemReq.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemReq.getProductId()));

            orderItem.setProduct(matchedProduct);
            orderItem.setPieces(Long.valueOf(itemReq.getPieces()));
            orderItem.setItemStatus(itemReq.getItemStatus());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(itemReq.getPrice());
            orderItem.setOrder(order); // associate item with order

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        return order;
    }

    public OrderResponse entityToResponse(Order order) {
        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getId());
        response.setOrderType(order.getOrderType());
        response.setOrderStatus(order.getStatus());
        response.setOriginAddress(order.getOriginAddress());
        response.setCarrier(order.getCarrier());
        response.setShipper(order.getShipper());
        response.setWarehouse(order.getWarehouse());
        response.setCustomerId(order.getCustomer().getId());

        List<OrderItemResponse> itemResponses = order.getOrderItems().stream().map(item -> {
            OrderItemResponse itemResp = new OrderItemResponse();

            itemResp.setProduct(item.getProduct());
            itemResp.setPieces(item.getPieces());
            itemResp.setItemStatus(item.getItemStatus());
            itemResp.setReferenceNumber(item.getReferenceNumber());
            itemResp.setShipmentDate(item.getShipmentDate());
            itemResp.setWarehouseName(item.getWarehouseName());
            itemResp.setStorageLocation(item.getStorageLocation());
            itemResp.setPalletNumber(item.getPalletNumber());
            itemResp.setStorageType(item.getStorageType());
            itemResp.setInventoryStatus(item.getInventoryStatus());
            itemResp.setExpiryDate(item.getExpiryDate());
            itemResp.setQuantity(item.getQuantity());
            itemResp.setPrice(item.getPrice());

            return itemResp;
        }).collect(Collectors.toList());

        response.setOrderItems(itemResponses);

        return response;
    }
}
