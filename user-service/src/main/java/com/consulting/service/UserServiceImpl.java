package com.consulting.service;

import com.consulting.constants.Role;
import com.consulting.dto.UserCreateRequest;
import com.consulting.dto.UserUpdateRequest;
import com.consulting.model.User;
import com.consulting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User createUser(UserCreateRequest userCreateRequest) {
        // Check if email already exists
        if (existsByEmail(userCreateRequest.getUserEmailId())) {
            throw new IllegalArgumentException("User with email " + userCreateRequest.getUserEmailId() + " already exists");
        }

        // Validate roles
        validateRoles(userCreateRequest.getRoles());

        // Create new user
        User user = new User();
        user.setUserEmailId(userCreateRequest.getUserEmailId());
        user.setUserName(userCreateRequest.getUserName());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));
        user.setRoles(userCreateRequest.getRoles());

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByUserEmailId(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(String userId, UserUpdateRequest updateRequest) {
        User user = getUserById(userId);

        // Update email if provided and not already taken
        if (updateRequest.getUserEmailId() != null &&
                !updateRequest.getUserEmailId().equals(user.getUserEmailId())) {

            if (existsByEmail(updateRequest.getUserEmailId())) {
                throw new IllegalArgumentException("Email " + updateRequest.getUserEmailId() + " is already in use");
            }
            user.setUserEmailId(updateRequest.getUserEmailId());
        }

        // Update name if provided
        if (updateRequest.getUserName() != null) {
            user.setUserName(updateRequest.getUserName());
        }

        // Update roles if provided
        if (updateRequest.getRoles() != null && !updateRequest.getRoles().isEmpty()) {
            validateRoles(updateRequest.getRoles());
            user.setRoles(updateRequest.getRoles());
        }

        return userRepository.save(user);
    }

    @Override
    public User updatePassword(String userId, String newPassword) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByUserEmailId(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String role) {
        Role roleEnum;
        try {
            roleEnum = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        return userRepository.findAll().stream()
                .filter(user -> user.getRoles() != null && user.getRoles().contains(roleEnum))
                .toList();
    }

    // Helper method to validate roles
    private void validateRoles(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("User must have at least one role");
        }

        for (Role role : roles) {
            if (role == null) {
                throw new IllegalArgumentException("Role cannot be null");
            }
        }
    }

    @Transactional(readOnly = true)
    public long getTotalUserCount() {
        return userRepository.count();
    }

    public User assignAdditionalRole(String userId, Role newRole) {
        User user = getUserById(userId);

        Set<Role> roles = user.getRoles();
        if (roles == null) {
            roles = Set.of(newRole);
        } else {
            roles.add(newRole);
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public User removeRole(String userId, Role roleToRemove) {
        User user = getUserById(userId);

        Set<Role> roles = user.getRoles();
        if (roles != null && !roles.isEmpty()) {
            roles.remove(roleToRemove);

            if (roles.isEmpty()) {
                throw new IllegalArgumentException("User must have at least one role");
            }

            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User authenticateUser(String email, String password) {
        User user = userRepository.findByUserEmailId(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public User updateUserFull(String userId, UserUpdateRequest updateRequest, String newPassword, Set<Role> newRoles) {
        User user = getUserById(userId);

        if (updateRequest.getUserEmailId() != null &&
                !updateRequest.getUserEmailId().equals(user.getUserEmailId())) {

            if (existsByEmail(updateRequest.getUserEmailId())) {
                throw new IllegalArgumentException("Email " + updateRequest.getUserEmailId() + " is already in use");
            }
            user.setUserEmailId(updateRequest.getUserEmailId());
        }

        if (updateRequest.getUserName() != null) {
            user.setUserName(updateRequest.getUserName());
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        if (newRoles != null && !newRoles.isEmpty()) {
            validateRoles(newRoles);
            user.setRoles(newRoles);
        }

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean hasRole(String userId, String role) {
        try {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            User user = getUserById(userId);
            return user.getRoles() != null && user.getRoles().contains(roleEnum);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByUserEmailId(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }
}