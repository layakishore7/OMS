package com.ordermanagement.Enum;

public class Enum {

    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        DISCONTINUED,
        OUT_OF_STOCK
    }

    public enum OrderType {
        PURCHASE,
        SALE,
        RETURN,
        ADJUSTMENT
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        RETURNED
    }
}
