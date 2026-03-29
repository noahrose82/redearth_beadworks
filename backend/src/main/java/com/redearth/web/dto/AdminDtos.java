package com.redearth.web.dto;

import java.time.Instant;

public class AdminDtos {

  public static class AdminOrderSummary {
    public Long id;
    public String status;
    public Integer totalCents;
    public Instant createdAt;

    public AdminOrderSummary(Long id, String status, Integer totalCents, Instant createdAt) {
      this.id = id;
      this.status = status;
      this.totalCents = totalCents;
      this.createdAt = createdAt;
    }
  }

  public static class UpdateOrderStatusRequest {
    public String status;
  }
}