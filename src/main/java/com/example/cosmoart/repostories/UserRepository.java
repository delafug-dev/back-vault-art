package com.example.cosmoart.repostories;

import com.example.cosmoart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    User findByUsernameOrEmail(String username, String email);
    List<User> findByIdIn(List<Long> userIds);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findAll();

}
