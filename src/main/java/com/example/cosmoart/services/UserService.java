package com.example.cosmoart.services;

import com.example.cosmoart.dto.UserUpdateRequestDTO;
import com.example.cosmoart.entities.User;
import com.example.cosmoart.exceptions.DuplicateResourceException;
import com.example.cosmoart.exceptions.ResourceNotFoundException;
import com.example.cosmoart.repostories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations including creation, update,
 * profile picture upload, and user data retrieval.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FileStorageService fileStorageService;

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el nombre: " + user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el email: " + user.getEmail());
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UserUpdateRequestDTO user, Long id) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));

        // Actualizar username solo si se recibe un valor válido y distinto
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            // Verificar si el nombre de usuario ya existe en otro usuario
            if (!userToUpdate.getUsername().equals(user.getUsername()) &&
                    userRepository.existsByUsername(user.getUsername())) {
                throw new DuplicateResourceException("Ya existe un usuario con el nombre: " + user.getUsername());
            }
            userToUpdate.setUsername(user.getUsername());
        }

        // Actualizar email solo si se recibe un valor válido y distinto
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!userToUpdate.getEmail().equals(user.getEmail()) &&
                    userRepository.existsByEmail(user.getEmail())) {
                throw new DuplicateResourceException("Ya existe un usuario con el email: " + user.getEmail());
            }
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            userToUpdate.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }

        userToUpdate.setBiography(user.getBiography());
        userToUpdate.setProfilePicture(user.getProfilePicture());

        return userRepository.save(userToUpdate);
    }

    public User uploadProfilePicture(Long id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
        String fileName = fileStorageService.storeFile(file);
        user.setProfilePicture(fileName);
        return userRepository.save(user);
    }


    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado" + username)));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email no encontrado" + email)));
    }

    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email);
    }

    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }
}
