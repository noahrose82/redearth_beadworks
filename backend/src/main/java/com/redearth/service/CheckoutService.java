package com.redearth.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.repo.OrderRepository;
import com.redearth.mysql.repo.PaymentRepository;
import com.redearth.security.SecurityUtil;
import com.redearth.web.dto.CheckoutDtos;
import com.redearth.web.error.NotFoundException;

@Service
public class CheckoutService {

  private final OrderRepository orders;
  private final PaymentRepository payments;

  public CheckoutService(OrderRepository orders, PaymentRepository payments) {
    this.orders = orders;
    this.payments = payments;
  }

  public CheckoutDtos.PaymentIntentResponse createIntent(CheckoutDtos.PaymentIntentRequest req) {
    Long userId = requireUser();

    OrderEntity order = orders.findById(req.orderId)
        .orElseThrow(() -> new NotFoundException("Order not found"));

    if (!order.getUserId().equals(userId) && !SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Order not found");
    }

    if (!order.getStatus().equals("NEW")) {
      throw new IllegalArgumentException("Order not in NEW status");
    }

    String intentId = "pi_" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    String clientSecret = intentId + "_secret_" + UUID.randomUUID().toString().replace("-", "");

    return new CheckoutDtos.PaymentIntentResponse(
        intentId,
        clientSecret,
        order.getTotalCents(),
        "STRIPE_STUB"
    );
  }

  @Transactional
  public CheckoutDtos.PaymentConfirmResponse confirm(CheckoutDtos.PaymentConfirmRequest req) {
    Long userId = requireUser();

    if (req == null || req.orderId == null) {
      throw new IllegalArgumentException("Invalid request");
    }

    OrderEntity order = orders.findById(req.orderId)
        .orElseThrow(() -> new NotFoundException("Order not found"));

    if (!order.getUserId().equals(userId) && !SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Order not found");
    }

    order.setStatus("PAID");
    orders.save(order);

    return new CheckoutDtos.PaymentConfirmResponse(
        order.getId(),
        order.getStatus(),
        req.intentId,
        "SUCCEEDED"
    );
  }

  private Long requireUser() {
    Long userId = SecurityUtil.currentUserId();
    if (userId == null) {
      throw new NotFoundException("Not authenticated");
    }
    return userId;
  }
}