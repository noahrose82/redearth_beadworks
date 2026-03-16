package com.redearth.security;

import com.redearth.mysql.entity.UserEntity;
import com.redearth.mysql.repo.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserRepository users;

  public JwtAuthFilter(JwtService jwtService, UserRepository users) {
    this.jwtService = jwtService;
    this.users = users;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        Claims claims = jwtService.parse(token).getBody();
        Object userIdClaim = claims.get("userId");
        Long userId = null;
        if (userIdClaim instanceof Integer i) {
          userId = i.longValue();
        } else if (userIdClaim instanceof Long l) {
          userId = l;
        } else if (userIdClaim instanceof String s) {
          userId = Long.valueOf(s);
        }

        if (userId != null) {
          UserEntity user = users.findById(userId).orElse(null);
          if (user != null) {
            var auth = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
          }
        }
      } catch (Exception ignored) {
        // invalid token -> continue unauthenticated
      }
    }
    filterChain.doFilter(request, response);
  }
}
