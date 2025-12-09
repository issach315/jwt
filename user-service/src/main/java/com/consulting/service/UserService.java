package com.consulting.service;

import com.consulting.dto.UserCreateRequest;
import com.consulting.dto.UserUpdateRequest;
import com.consulting.model.User;
import com.consulting.constants.Role;

import java.util.List;
import java.util.Set;

public interface UserService {

    User createUser(UserCreateRequest userCreateRequest);

    User getUserById(String userId);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(String userId, UserUpdateRequest updateRequest);

    User updatePassword(String userId, String newPassword);

    void deleteUser(String userId);

    boolean existsByEmail(String email);

    List<User> getUsersByRole(String role);

    // Add these to your interface
    User authenticateUser(String email, String password);

    User findByEmail(String email);

    boolean hasRole(String userId, String role);

    // Optional: You can add these if you want them in the interface
    long getTotalUserCount();

    User assignAdditionalRole(String userId, Role newRole);

    User removeRole(String userId, Role roleToRemove);

    User updateUserFull(String userId, UserUpdateRequest updateRequest, String newPassword, Set<Role> newRoles);
}