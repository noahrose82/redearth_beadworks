package com.redearth.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.redearth.service.AuthService;
import com.redearth.web.dto.AuthRequests;
import com.redearth.web.dto.AuthResponses;
import com.redearth.web.error.NotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponses.MeResponse register(@Valid @RequestBody AuthRequests.RegisterRequest req) {
    var user = auth.register(req.email, req.fullName, req.password);
    return new AuthResponses.MeResponse(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        user.getRole()
    );
  }

  @PostMapping("/login")
  public AuthResponses.LoginResponse login(@Valid @RequestBody AuthRequests.LoginRequest req) {
    return auth.login(req.email, req.password);
  }

  @GetMapping("/me")
  public AuthResponses.MeResponse me() {
    Long userId = resolveCurrentUserId();
    var user = auth.getById(userId);

    return new AuthResponses.MeResponse(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        user.getRole()
    );
  }

  private Long resolveCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new NotFoundException("Not authenticated");
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof Long id) {
      return id;
    }

    if (principal instanceof String str) {
      if ("anonymousUser".equals(str) || str.isBlank()) {
        throw new NotFoundException("Not authenticated");
      }

      try {
        return Long.parseLong(str);
      } catch (NumberFormatException ex) {
        throw new NotFoundException("Authenticated user id is not numeric");
      }
    }

    Object details = authentication.getDetails();
    if (details instanceof Long id) {
      return id;
    }

    if (details instanceof String str) {
      try {
        return Long.parseLong(str);
      } catch (NumberFormatException ignored) {
      }
    }

    String name = authentication.getName();
    if (name != null && !name.isBlank() && !"anonymousUser".equals(name)) {
      try {
        return Long.parseLong(name);
      } catch (NumberFormatException ex) {
        throw new NotFoundException("Authenticated user id is not numeric");
      }
    }

    throw new NotFoundException("Not authenticated");
  }
}