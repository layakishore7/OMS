package com.ordermanagement.entity;

import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "inventory_history")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryHistory extends BaseEntity {

    @Enumerated(EnumType.STRING)  // MUST specify the enum type
    @Column(name = "action")
    private Enum.Action action;

    @Column(name = "quantity")
    private Integer quantity;  // Use Integer instead of int

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "reason")
    private String reason;

    @Column(name = "updated_user")
    private String updatedUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}