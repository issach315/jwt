package com.consulting.controller;

import com.consulting.model.User;
import com.consulting.service.UserService;
import com.consulting.dto.UserCreateRequest;
import com.consulting.dto.UserUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Allow registration without authentication
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Helper method to get current user's email
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // Returns the email (username)
    }

    // Custom method for @PreAuthorize to check if current user owns the resource
    public boolean isCurrentUserOwner(String userId) {
        try {
            String currentUserEmail = getCurrentUserEmail();
            User currentUser = userService.getUserByEmail(currentUserEmail);
            User targetUser = userService.getUserById(userId);

            // Check if the current user's email matches the target user's email
            return currentUser.getUserEmailId().equals(targetUser.getUserEmailId());
        } catch (Exception e) {
            return false;
        }
    }

    // All users can view their own profile
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userController.isCurrentUserOwner(#userId)")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Update - only admin or same user
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or @userController.isCurrentUserOwner(#userId)")
    public ResponseEntity<User> updateUser(@PathVariable String userId,
                                           @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(userId, request);
        return ResponseEntity.ok(user);
    }

    // Delete - only admin
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Get all users - only admin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Update password - only admin or same user
    @PutMapping("/{userId}/password")
    @PreAuthorize("hasRole('ADMIN') or @userController.isCurrentUserOwner(#userId)")
    public ResponseEntity<User> updatePassword(@PathVariable String userId,
                                               @RequestBody String newPassword) {
        User user = userService.updatePassword(userId, newPassword);
        return ResponseEntity.ok(user);
    }

    // Get user by email - admin only
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Get users by role - admin only
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
}