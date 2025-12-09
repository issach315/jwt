// AuthenticationRequest.java
package com.consulting.dto;

public class AuthenticationRequest {
    private String userEmailId;
    private String password;

    // Constructors
    public AuthenticationRequest() {}

    public AuthenticationRequest(String userEmailId, String password) {
        this.userEmailId = userEmailId;
        this.password = password;
    }

    // Getters and Setters
    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


