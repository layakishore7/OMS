package com.ordermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order extends BaseEntity {

    @Column(name="order_number")
    private String orderNumber;

    @Column(name="order_type")
    @Enumerated(EnumType.ORDINAL)
    private Enum.OrderType orderType;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "shipper")
    private String shipper;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "origin_address")
    private String originAddress;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "order_received_date")
    private LocalDateTime orderReceivedDate;

    @Column(name = "order_dispatched_date")
    private LocalDateTime orderDispatchedDate;

    @Enumerated(EnumType.STRING)
    private Enum.OrderStatus status;

    @Enumerated(EnumType.ORDINAL)
    private Enum.Status recordStatus;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;
}