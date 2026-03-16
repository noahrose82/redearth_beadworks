package com.redearth.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
  private String issuer;
  private String secret;
  private int expirationMinutes;

  public String getIssuer() { return issuer; }
  public void setIssuer(String issuer) { this.issuer = issuer; }
  public String getSecret() { return secret; }
  public void setSecret(String secret) { this.secret = secret; }
  public int getExpirationMinutes() { return expirationMinutes; }
  public void setExpirationMinutes(int expirationMinutes) { this.expirationMinutes = expirationMinutes; }
}
