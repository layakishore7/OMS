package com.ordermanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordermanagement.Enum.Enum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "categories")
@Builder
public class Category extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "shipper_id")
    private Organization shipperOrganization;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

}
