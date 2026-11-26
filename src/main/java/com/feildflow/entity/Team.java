package com.feildflow.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name="teams")
@Getter 
@Setter 
@NoArgsConstructor 
public class Team {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String teamName;

    private String department;

    private String desciption;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
