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

    public enum Status {
        ACTIVE,
        INACTIVE
    }

    public enum StorageType {
        RACK,
        FLOOR,
        COLD_STORAGE
    }

    public enum InventoryStatus {
        NEW,
        ON_HOLD,
        DAMAGED,
        STAGING,
        COMPLETED
    }

    public enum Action {
        ADDED,
        REMOVED
    }
}
