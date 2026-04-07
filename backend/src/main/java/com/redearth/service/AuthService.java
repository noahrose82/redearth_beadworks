package com.redearth.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.redearth.mysql.entity.UserEntity;
import com.redearth.mysql.repo.UserRepository;
import com.redearth.security.JwtService;
import com.redearth.web.dto.AuthResponses;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtService jwtService;

  public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwtService) {
    this.users = users;
    this.encoder = encoder;
    this.jwtService = jwtService;
  }

  public AuthResponses.LoginResponse login(String email, String password) {
    String normalizedEmail = email.trim().toLowerCase();

    UserEntity user = users.findByEmail(normalizedEmail)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

    if (!encoder.matches(password, user.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    String token = jwtService.issue(user);
    return new AuthResponses.LoginResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole());
  }

  public UserEntity register(String email, String fullName, String password) {
    String e = email.trim().toLowerCase();
    if (users.existsByEmail(e)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
    }

    UserEntity u = new UserEntity();
    u.setEmail(e);
    u.setFullName(fullName.trim());
    u.setPasswordHash(encoder.encode(password));
    u.setRole("CUSTOMER");
    return users.save(u);
  }

  public UserEntity getById(Long id) {
    return users.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
  }
}