package com.example.tasktracker.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false,unique = true, length = 100)
    private String email;

    @Column(name = "keycloak_id",nullable = false, unique = true)
    private String keycloakId;

    @Column(name = "created_at", nullable = false, updatable = false )
    private LocalDateTime createdAt;

    @Column(name = "updated_at" )
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
