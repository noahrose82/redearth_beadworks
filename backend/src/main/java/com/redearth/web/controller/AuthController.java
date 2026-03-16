package com.redearth.web.controller;

import com.redearth.security.SecurityUtil;
import com.redearth.service.AuthService;
import com.redearth.web.dto.AuthRequests;
import com.redearth.web.dto.AuthResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    return new AuthResponses.MeResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
  }

  @PostMapping("/login")
  public AuthResponses.LoginResponse login(@Valid @RequestBody AuthRequests.LoginRequest req) {
    return auth.login(req.email, req.password);
  }

  @GetMapping("/me")
  public AuthResponses.MeResponse me() {
    Long userId = SecurityUtil.currentUserId();
    if (userId == null) throw new com.redearth.web.error.NotFoundException("Not authenticated");
    var user = auth.getById(userId);
    return new AuthResponses.MeResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole());
  }
}
