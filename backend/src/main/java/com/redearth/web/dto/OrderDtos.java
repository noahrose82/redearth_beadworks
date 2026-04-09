package com.redearth.web.dto;

import java.time.Instant;
import java.util.List;

import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.entity.OrderItemEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderDtos {

    public static class CreateOrderRequest {
        @NotEmpty
        @Valid
        public List<CreateOrderItem> items;
    }

    public static class CreateOrderItem {
        @NotBlank
        public String productId;

        @NotNull
        @Min(1)
        public Integer quantity;
    }

    public static class OrderSummary {
        public Long id;
        public String status;
        public Integer totalCents;
        public Instant createdAt;

        public OrderSummary() {
        }

        public OrderSummary(Long id, String status, Integer totalCents, Instant createdAt) {
            this.id = id;
            this.status = status;
            this.totalCents = totalCents;
            this.createdAt = createdAt;
        }
    }

    public static class OrderItemDetail {
        public String productId;
        public String productName;
        public Integer unitPriceCents;
        public Integer quantity;

        public OrderItemDetail() {
        }

        public OrderItemDetail(String productId, String productName, Integer unitPriceCents, Integer quantity) {
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
        public List<OrderItemDetail> items;

        public OrderDetail() {
        }

        public OrderDetail(Long id, String status, Integer totalCents, Instant createdAt, List<OrderItemDetail> items) {
            this.id = id;
            this.status = status;
            this.totalCents = totalCents;
            this.createdAt = createdAt;
            this.items = items;
        }

        public static OrderDetail from(OrderEntity order, List<OrderItemEntity> orderItems) {
            List<OrderItemDetail> mappedItems = orderItems.stream()
                    .map(i -> new OrderItemDetail(
                            i.getProductId(),
                            i.getProductName(),
                            i.getUnitPriceCents(),
                            i.getQuantity()))
                    .toList();

            return new OrderDetail(
                    order.getId(),
                    order.getStatus(),
                    order.getTotalCents(),
                    order.getCreatedAt(),
                    mappedItems);
        }
    }
}