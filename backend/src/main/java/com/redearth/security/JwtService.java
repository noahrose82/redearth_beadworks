package com.redearth.security;

import com.redearth.mysql.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  private static final String FALLBACK_SECRET =
      "redearth_fallback_secret_key_must_be_32+chars_long_!!_OK";

  private final JwtProperties props;
  private final SecretKey key;

  public JwtService(JwtProperties props) {
    this.props = props;

    String secret = props != null ? props.getSecret() : null;
    if (secret == null || secret.trim().isEmpty()) {
      secret = FALLBACK_SECRET;
      System.out.println("WARNING: app.jwt.secret was null/empty. Using fallback JWT secret for local dev.");
    }

    SecretKey builtKey;
    try {
      byte[] decoded = Decoders.BASE64.decode(secret);
      if (decoded.length >= 32) {
        builtKey = Keys.hmacShaKeyFor(decoded);
      } else {
        builtKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
      }
    } catch (Exception ex) {
      builtKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    this.key = builtKey;
  }

  public String issue(UserEntity user) {
    String issuer = (props != null && props.getIssuer() != null && !props.getIssuer().isBlank())
        ? props.getIssuer()
        : "redearth-beadworks";

    int minutes = (props != null && props.getExpirationMinutes() > 0)
        ? props.getExpirationMinutes()
        : 120;

    Date now = new Date();
    Date exp = new Date(now.getTime() + minutes * 60L * 1000L);

    String subject = (user.getEmail() != null && !user.getEmail().isBlank())
        ? user.getEmail()
        : String.valueOf(user.getId());

    return Jwts.builder()
        .setIssuer(issuer)
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(exp)
        .addClaims(Map.of(
            "userId", user.getId(),
            "email", user.getEmail() == null ? "" : user.getEmail(),
            "role", user.getRole() == null ? "CUSTOMER" : user.getRole()
        ))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Jws<Claims> parse(String token) {
    String issuer = (props != null && props.getIssuer() != null && !props.getIssuer().isBlank())
        ? props.getIssuer()
        : "redearth-beadworks";

    return Jwts.parserBuilder()
        .setSigningKey(key)
        .requireIssuer(issuer)
        .build()
        .parseClaimsJws(token);
  }
}
