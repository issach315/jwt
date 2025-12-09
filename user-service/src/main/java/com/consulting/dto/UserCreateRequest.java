package com.consulting.dto;

import com.consulting.constants.Role;

import java.util.Set;

public class UserCreateRequest {
    private String userEmailId;
    private String userName;
    private String password;
    private Set<Role> roles;

    // Constructors
    public UserCreateRequest() {}

    public UserCreateRequest(String userEmailId, String userName, String password, Set<Role> roles) {
        this.userEmailId = userEmailId;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
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
}