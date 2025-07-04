package com.ordermanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer  Id;
    @Column(name = "created_userid")
    private Long createdUserId;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_userid")
    private Long updatedUserId;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
