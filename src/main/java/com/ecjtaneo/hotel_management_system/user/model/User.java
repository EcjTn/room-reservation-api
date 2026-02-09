package com.ecjtaneo.hotel_management_system.user.model;

import com.ecjtaneo.hotel_management_system.auth.model.RefreshToken;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.GUEST;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    private LocalDateTime created_at = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    List<RefreshToken> refreshTokens;
}