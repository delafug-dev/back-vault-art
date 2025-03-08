package com.example.cosmoart.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    private String username;
    private String email;
    private String passwordHash;
    private String biography;
    private String profilePicture;
}
