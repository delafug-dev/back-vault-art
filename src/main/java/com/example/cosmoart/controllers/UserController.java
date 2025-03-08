package com.example.cosmoart.controllers;

import com.example.cosmoart.dto.UserResponseDTO;
import com.example.cosmoart.dto.UserUpdateRequestDTO;
import com.example.cosmoart.entities.User;
import com.example.cosmoart.mapper.UserMapper;
import com.example.cosmoart.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UserController is a RESTful web controller that provides endpoints
 * for managing user-related operations such as user registration,
 * profile updates, uploading profile pictures, and retrieving all users.
 *
 * This controller uses UserService for business logic and UserMapper
 * for mapping entities to DTOs.
 *
 * It handles requests for the "/api/users" path.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(userMapper.toDTO(savedUser));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDTO updateRequest
    ) {
        User updatedUser = userService.updateUser(updateRequest, id);
        return ResponseEntity.ok(userMapper.toDTO(updatedUser));
    }

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<User> uploadProfilePicture(
            @RequestParam("userId") Long userId,
            @RequestParam("image") MultipartFile image) {
        User updatedUser = userService.uploadProfilePicture(userId, image);
        return ResponseEntity.ok(updatedUser);
    }


    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }




}
