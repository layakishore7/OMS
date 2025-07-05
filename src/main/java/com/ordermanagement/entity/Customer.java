package com.ordermanagement.entity;


import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends BaseEntity{

    @Column(name = "customer_name")
    private String CustomerName;

    private String email;

    private String phone;

    private String address;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="status")
    private Enum.Status organizationStatus;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
