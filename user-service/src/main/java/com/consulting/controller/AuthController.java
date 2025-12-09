package com.consulting.controller;

import com.consulting.dto.AuthenticationRequest;
import com.consulting.dto.AuthenticationResponse;
import com.consulting.dto.RefreshTokenRequest;
import com.consulting.model.User;
import com.consulting.service.CustomUserDetailsService;
import com.consulting.service.JwtTokenUtil;
import com.consulting.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserEmailId(),
                            request.getPassword()
                    )
            );

            // Generate tokens
            final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserEmailId());
            final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
            final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Get user details
            User user = userService.findByEmail(request.getUserEmailId());

            // Calculate expiration time in seconds
            Long expiresIn = jwtTokenUtil.extractExpiration(accessToken).getTime() / 1000;

            // Return response with both tokens
            return ResponseEntity.ok(new AuthenticationResponse(
                    accessToken,
                    refreshToken,
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmailId(),
                    expiresIn
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        try {
            // Extract username from refresh token
            String username = jwtTokenUtil.extractUsername(refreshToken);

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate refresh token
            if (!jwtTokenUtil.validateRefreshToken(refreshToken, userDetails)) {
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }

            // Generate new access token
            String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);

            // Optionally generate new refresh token (rotate refresh token)
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

            // Get user details
            User user = userService.findByEmail(username);

            // Calculate expiration time in seconds
            Long expiresIn = jwtTokenUtil.extractExpiration(newAccessToken).getTime() / 1000;

            // Return new tokens
            return ResponseEntity.ok(new AuthenticationResponse(
                    newAccessToken,
                    newRefreshToken, // Return new refresh token (rotated)
                    user.getUserId(),
                    user.getUserName(),
                    user.getUserEmailId(),
                    expiresIn
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid refresh token: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                    @RequestBody(required = false) RefreshTokenRequest refreshTokenRequest) {
        // In a stateless JWT implementation, logout is handled client-side
        // But you can implement token blacklisting or use a token store if needed
        return ResponseEntity.ok("Logged out successfully. Please remove tokens from client storage.");
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtTokenUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    return ResponseEntity.ok("Token is valid");
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid token");
            }
        }
        return ResponseEntity.badRequest().body("No token provided");
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtTokenUtil.extractUsername(token);
            User user = userService.findByEmail(username);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }
}