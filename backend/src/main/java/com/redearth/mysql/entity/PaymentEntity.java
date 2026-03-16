package com.redearth.mysql.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "payments")
public class PaymentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false)
  private Long orderId;

  @Column(nullable = false)
  private String provider; // STRIPE (stub/local)

  @Column(name = "provider_intent_id", nullable = false)
  private String providerIntentId;

  @Column(nullable = false)
  private String status; // CREATED, SUCCEEDED, FAILED

  @Column(name = "amount_cents", nullable = false)
  private Integer amountCents;

  @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
  private Instant createdAt;

  public Long getId() { return id; }
  public Long getOrderId() { return orderId; }
  public String getProvider() { return provider; }
  public String getProviderIntentId() { return providerIntentId; }
  public String getStatus() { return status; }
  public Integer getAmountCents() { return amountCents; }
  public Instant getCreatedAt() { return createdAt; }

  public void setId(Long id) { this.id = id; }
  public void setOrderId(Long orderId) { this.orderId = orderId; }
  public void setProvider(String provider) { this.provider = provider; }
  public void setProviderIntentId(String providerIntentId) { this.providerIntentId = providerIntentId; }
  public void setStatus(String status) { this.status = status; }
  public void setAmountCents(Integer amountCents) { this.amountCents = amountCents; }
}
