package com.ordermanagement.Enum;

public class Enum {

    public enum ProductStatus {
        ACTIVE,
        INACTIVE,
        DISCONTINUED,
        OUT_OF_STOCK
    }

    public enum OrderType {
        INBOUND,
        OUTBOUND,
        TRANSFER
    }

    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED,
        RETURNED,
        NEW
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
        RACK(0),FLOOR(1),COLD_STORAGE(2);
        private final int value;
        StorageType(int value) {
            this.value=value;
        }
        public int getValue() {
            return value;
        }
        public static StorageType fromValues(int value) {
            for (StorageType storageType: StorageType.values()) {
                if (storageType.getValue()==value) {
                    return storageType;
                }
            }
            throw new IllegalArgumentException("Invalid StorageType "+ value);
        }
    }

    public enum InventoryStatus {
        NEW(0), ON_HOLD(1),DAMAGED(2),STAGING(3),COMPLETED(4);
        private final int value;
        InventoryStatus(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
        public static InventoryStatus fromValues(int value) {
            for (InventoryStatus inventoryStatus: InventoryStatus.values()){
                if (inventoryStatus.getValue()==value){
                    return inventoryStatus;
                }
            }
            throw new IllegalArgumentException(("Invalid Inventory Status "+value));
        }
    }

    public enum Action {
        ADDED,
        REMOVED
    }
}
