package com.consulting.dto;

public class UserUpdateRequest {
    private String userName;
    private String userEmailId;

    // Constructors
    public UserUpdateRequest() {}

    public UserUpdateRequest(String userName, String userEmailId) {
        this.userName = userName;
        this.userEmailId = userEmailId;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }
}