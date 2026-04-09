package com.redearth.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
  private SecurityUtil() {}

  public static Long currentUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
      return null;
    }

    Object principal = auth.getPrincipal();

    if (principal instanceof Long id) {
      return id;
    }

    if (principal instanceof Integer id) {
      return id.longValue();
    }

    if (principal instanceof String str) {
      if (str.isBlank() || "anonymousUser".equals(str)) {
        return null;
      }

      try {
        return Long.parseLong(str);
      } catch (NumberFormatException e) {
        return null;
      }
    }

    return null;
  }

  public static boolean hasRole(String role) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return false;
    }

    return auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
  }
}