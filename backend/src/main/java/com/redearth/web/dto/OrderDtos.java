package com.redearth.web.dto;

import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.entity.OrderItemEntity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public class OrderDtos {

  public static class CartItem {
    @NotNull public String productId;
    @NotNull public Integer quantity;
  }

  public static class CreateOrderRequest {
    @NotEmpty public List<CartItem> items;
  }

  public static class OrderSummary {
    public Long id;
    public String status;
    public Integer totalCents;
    public Instant createdAt;

    public OrderSummary(Long id, String status, Integer totalCents, Instant createdAt) {
      this.id = id;
      this.status = status;
      this.totalCents = totalCents;
      this.createdAt = createdAt;
    }
  }

  public static class OrderItem {
    public String productId;
    public String productName;
    public Integer unitPriceCents;
    public Integer quantity;

    public OrderItem(String productId, String productName, Integer unitPriceCents, Integer quantity) {
      this.productId = productId;
      this.productName = productName;
      this.unitPriceCents = unitPriceCents;
      this.quantity = quantity;
    }
  }

  public static class OrderDetail {
    public Long id;
    public String status;
    public Integer totalCents;
    public Instant createdAt;
    public List<OrderItem> items;

    public static OrderDetail from(OrderEntity o, List<OrderItemEntity> entities) {
      OrderDetail d = new OrderDetail();
      d.id = o.getId();
      d.status = o.getStatus();
      d.totalCents = o.getTotalCents();
      d.createdAt = o.getCreatedAt();
      d.items = entities.stream()
          .map(i -> new OrderItem(i.getProductId(), i.getProductName(), i.getUnitPriceCents(), i.getQuantity()))
          .toList();
      return d;
    }
  }
}
