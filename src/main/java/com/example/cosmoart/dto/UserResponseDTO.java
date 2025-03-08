package com.example.cosmoart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private LocalDateTime createdAt;
    private String role;
    private String biography;
    private String profilePicture;

}
