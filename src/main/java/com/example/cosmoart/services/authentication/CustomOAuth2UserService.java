package com.example.cosmoart.services.authentication;

import com.example.cosmoart.entities.Role;
import com.example.cosmoart.entities.User;
import com.example.cosmoart.repostories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        String surname = oAuth2User.getAttribute("family_name");
        String picture = oAuth2User.getAttribute("picture");

        // Check if user exists in database
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            // Create new user
            User user = User.builder()
                    .email(email)
                    .name(name != null ? name : "Google User")
                    .surname(surname != null ? surname : "")
                    .username(generateUsername(email))
                    .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .role(Role.USER)
                    .profilePicture(picture)
                    .build();

            userRepository.save(user);
        } else {
            // Update existing user's Google info if needed
            User existingUser = userOptional.get();
            if (picture != null && (existingUser.getProfilePicture() == null || existingUser.getProfilePicture().isEmpty())) {
                existingUser.setProfilePicture(picture);
                userRepository.save(existingUser);
            }
        }

        return oAuth2User;
    }

    private String generateUsername(String email) {
        // Generate username from email
        String baseName = email.substring(0, email.indexOf('@'));
        String username = baseName;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseName + counter;
            counter++;
        }

        return username;
    }

}
