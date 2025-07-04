package com.ordermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(name = "category_name")
    private String CategoryName;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Enum.Status status;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;

}
