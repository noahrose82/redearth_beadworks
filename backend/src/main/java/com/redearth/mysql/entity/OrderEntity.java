package com.redearth.mysql.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  private String status; // NEW, PAID, SHIPPED, CANCELLED

  @Column(name = "total_cents", nullable = false)
  private Integer totalCents;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  public Long getId() { return id; }
  public Long getUserId() { return userId; }
  public String getStatus() { return status; }
  public Integer getTotalCents() { return totalCents; }
  public Instant getCreatedAt() { return createdAt; }

  public void setId(Long id) { this.id = id; }
  public void setUserId(Long userId) { this.userId = userId; }
  public void setStatus(String status) { this.status = status; }
  public void setTotalCents(Integer totalCents) { this.totalCents = totalCents; }
}
