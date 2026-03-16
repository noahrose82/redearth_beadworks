package com.redearth.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CheckoutDtos {

  public static class PaymentIntentRequest {
    @NotNull public Long orderId;
  }

  public static class PaymentIntentResponse {
    public String intentId;
    public String clientSecret;
    public Integer amountCents;
    public String provider;

    public PaymentIntentResponse(String intentId, String clientSecret, Integer amountCents, String provider) {
      this.intentId = intentId;
      this.clientSecret = clientSecret;
      this.amountCents = amountCents;
      this.provider = provider;
    }
  }

  public static class PaymentConfirmRequest {
    @NotNull public Long orderId;
    @NotBlank public String intentId;
  }

  public static class PaymentConfirmResponse {
    public Long orderId;
    public String orderStatus;
    public String intentId;
    public String paymentStatus;

    public PaymentConfirmResponse(Long orderId, String orderStatus, String intentId, String paymentStatus) {
      this.orderId = orderId;
      this.orderStatus = orderStatus;
      this.intentId = intentId;
      this.paymentStatus = paymentStatus;
    }
  }
}
