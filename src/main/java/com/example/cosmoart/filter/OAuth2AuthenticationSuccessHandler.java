package com.example.cosmoart.filter;

        import com.example.cosmoart.entities.User;
        import com.example.cosmoart.repostories.UserRepository;
        import com.example.cosmoart.services.authentication.JwtService;
        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.servlet.http.HttpServletResponse;
        import lombok.RequiredArgsConstructor;
        import org.springframework.security.core.Authentication; // Correct import
        import org.springframework.security.oauth2.core.user.OAuth2User;
        import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
        import org.springframework.stereotype.Component;

        import java.io.IOException;

        @Component
        @RequiredArgsConstructor
        public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

            private final JwtService jwtService;
            private final UserRepository userRepository;

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException {
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");

                // Find the user by email
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new IllegalStateException("User not found with email: " + email));

                // Generate JWT token
                String token = jwtService.generateToken(user.getUsername());

                // Set token in response or redirect with token
                response.setContentType("application/json");
                response.getWriter().write("{\"token\":\"" + token + "\"}");
            }
        }