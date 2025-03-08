package com.example.cosmoart.mapper;

import com.example.cosmoart.dto.UserResponseDTO;
import com.example.cosmoart.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .role(user.getRole().name())
                .biography(user.getBiography())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}