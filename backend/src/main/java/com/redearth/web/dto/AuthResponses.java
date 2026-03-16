package com.redearth.web.dto;

public class AuthResponses {
  public static class LoginResponse {
    public String token;
    public Long userId;
    public String email;
    public String fullName;
    public String role;

    public LoginResponse(String token, Long userId, String email, String fullName, String role) {
      this.token = token;
      this.userId = userId;
      this.email = email;
      this.fullName = fullName;
      this.role = role;
    }
  }

  public static class MeResponse {
    public Long userId;
    public String email;
    public String fullName;
    public String role;

    public MeResponse(Long userId, String email, String fullName, String role) {
      this.userId = userId;
      this.email = email;
      this.fullName = fullName;
      this.role = role;
    }
  }
}
