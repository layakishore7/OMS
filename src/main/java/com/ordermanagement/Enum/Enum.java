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
        INACTIVE(0),ACTIVE(1);
        private final int value;
        Status(int value) {
            this.value=value;
        }
        public int getValue() {
            return value;
        }
        public static Status fromValue(int value) {
            for (Status status : Status.values()) {
                if (status.getValue()==value){
                    return status;
                }
            }
            throw new IllegalArgumentException(("Invalid Status "+ value));
        }
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
