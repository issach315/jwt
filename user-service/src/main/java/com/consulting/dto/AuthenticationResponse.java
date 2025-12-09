package com.consulting.dto;

public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken; // Add refresh token
    private String tokenType = "Bearer";
    private String userId;
    private String userName;
    private String userEmailId;
    private Long expiresIn; // Token expiration time in seconds

    public AuthenticationResponse() {}

    // Constructor with refresh token
    public AuthenticationResponse(String accessToken, String refreshToken,
                                  String userId, String userName, String userEmailId,
                                  Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.userName = userName;
        this.userEmailId = userEmailId;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}