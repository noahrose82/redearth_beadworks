package com.redearth.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthRequests {

  public static class RegisterRequest {
    @Email @NotBlank public String email;
    @NotBlank public String fullName;
    @NotBlank public String password;
  }

  public static class LoginRequest {
    @Email @NotBlank public String email;
    @NotBlank public String password;
  }
}
