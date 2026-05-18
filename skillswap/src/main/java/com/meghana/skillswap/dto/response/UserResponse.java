package com.meghana.skillswap.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String location;

    private String bio;

    private String profilePicture;

    private String headline;

    private Double rating;

    private String role;
}