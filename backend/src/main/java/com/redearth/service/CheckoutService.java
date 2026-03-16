package com.redearth.service;

import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.entity.PaymentEntity;
import com.redearth.mysql.repo.OrderRepository;
import com.redearth.mysql.repo.PaymentRepository;
import com.redearth.security.SecurityUtil;
import com.redearth.web.dto.CheckoutDtos;
import com.redearth.web.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    OrderEntity order = orders.findById(req.orderId).orElseThrow(() -> new NotFoundException("Order not found"));

    if (!order.getUserId().equals(userId) && !SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Order not found");
    }
    if (!order.getStatus().equals("NEW")) {
      throw new IllegalArgumentException("Order not in NEW status");
    }

    // Stripe stub: generate a fake intent id + client secret
    String intentId = "pi_" + UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    String clientSecret = intentId + "_secret_" + UUID.randomUUID().toString().replace("-", "");

    return new CheckoutDtos.PaymentIntentResponse(intentId, clientSecret, order.getTotalCents(), "STRIPE_STUB");
  }

  @Transactional
  public CheckoutDtos.PaymentConfirmResponse confirm(CheckoutDtos.PaymentConfirmRequest req) {
    Long userId = requireUser();
    OrderEntity order = orders.findById(req.orderId).orElseThrow(() -> new NotFoundException("Order not found"));

    if (!order.getUserId().equals(userId) && !SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Order not found");
    }

    PaymentEntity p = new PaymentEntity();
    p.setOrderId(order.getId());
    p.setProvider("STRIPE_STUB");
    p.setProviderIntentId(req.intentId);
    p.setStatus("SUCCEEDED");
    p.setAmountCents(order.getTotalCents());
    payments.save(p);

    order.setStatus("PAID");
    orders.save(order);

    return new CheckoutDtos.PaymentConfirmResponse(order.getId(), order.getStatus(), p.getProviderIntentId(), p.getStatus());
  }

  private Long requireUser() {
    Long userId = SecurityUtil.currentUserId();
    if (userId == null) throw new NotFoundException("Not authenticated");
    return userId;
  }
}
