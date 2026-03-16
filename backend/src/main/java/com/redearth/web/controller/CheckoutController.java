package com.redearth.web.controller;

import com.redearth.service.CheckoutService;
import com.redearth.web.dto.CheckoutDtos;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

  private final CheckoutService checkout;

  public CheckoutController(CheckoutService checkout) {
    this.checkout = checkout;
  }

  @PostMapping("/intent")
  public CheckoutDtos.PaymentIntentResponse createIntent(@Valid @RequestBody CheckoutDtos.PaymentIntentRequest req) {
    return checkout.createIntent(req);
  }

  @PostMapping("/confirm")
  public CheckoutDtos.PaymentConfirmResponse confirm(@Valid @RequestBody CheckoutDtos.PaymentConfirmRequest req) {
    return checkout.confirm(req);
  }
}
