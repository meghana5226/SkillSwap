package com.meghana.skillswap.entity;

import com.meghana.skillswap.entity.enums.Role;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(
            nullable = false,
            unique = true
    )
    private String email;

    @Column(nullable = false)
    private String password;

    private String location;

    @Column(length = 1000)
    private String bio;

    private String profilePicture;

    private String headline;

    @Column(nullable = false)
@Builder.Default
private Double rating = 0.0;
    @Enumerated(EnumType.STRING)
    private Role role;
}