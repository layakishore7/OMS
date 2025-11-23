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
