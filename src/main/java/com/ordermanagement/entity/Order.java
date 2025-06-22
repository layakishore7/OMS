package com.ordermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private Enum.OrderType orderType;

    @Enumerated(EnumType.STRING)
    private Enum.OrderStatus status;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
    }
}
