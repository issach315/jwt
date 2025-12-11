package com.consulting.dto;

import com.consulting.constants.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;


public class UserUpdateRequest {
    @NotBlank(message = "User name is required")
    private String userName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String userEmailId;

    private Set<Role> roles;

    public UserUpdateRequest() {
    }

    public UserUpdateRequest(Set<Role> roles, String userEmailId, String userName) {
        this.roles = roles;
        this.userEmailId = userEmailId;
        this.userName = userName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(@Email(message = "Invalid email format") @NotBlank(message = "Email is required") String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public @NotBlank(message = "User name is required") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "User name is required") String userName) {
        this.userName = userName;
    }
    // Add this field for updating roles
}