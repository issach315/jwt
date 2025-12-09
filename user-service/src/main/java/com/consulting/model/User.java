package com.consulting.model;

import com.consulting.constants.Role;
import com.consulting.service.UserIdService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Set;

@Entity
@Table(name = "users")
@EntityListeners(User.UserEntityListener.class)
public class User {

    @Id
    private String userId;

    private String userEmailId;

    private String userName;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Constructor
    public User() {
        // Default constructor
    }

    // Getter and setters (updated to include setUserId)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    @PrePersist
    public void prePersist() {
        if (this.userId == null) {
            // Generate user ID if not already set
            this.userId = "USR" + System.currentTimeMillis();
        }
    }

    // Entity Listener class
    public static class UserEntityListener {

        private static UserIdService userIdService;

        // Static method to set the service (called by Spring)
        @Autowired
        public void setUserIdService(@Lazy UserIdService userIdService) {
            UserEntityListener.userIdService = userIdService;
        }

        @PrePersist
        public void prePersist(User user) {
            if (user.getUserId() == null) {
                user.setUserId(userIdService.generateNextUserId());
            }
        }


    }
}