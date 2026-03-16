package com.redearth.mysql.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String role; // ADMIN or CUSTOMER

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  public Long getId() { return id; }
  public String getEmail() { return email; }
  public String getFullName() { return fullName; }
  public String getPasswordHash() { return passwordHash; }
  public String getRole() { return role; }
  public Instant getCreatedAt() { return createdAt; }

  public void setId(Long id) { this.id = id; }
  public void setEmail(String email) { this.email = email; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public void setRole(String role) { this.role = role; }
}
