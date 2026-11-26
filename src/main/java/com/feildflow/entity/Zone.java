package com.feildflow.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="zones")
@Getter 
@Setter 
@NoArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String zoneName;

    private String area;

    private String description;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
