package com.redearth.web.controller;

import com.redearth.service.OrdersService;
import com.redearth.web.dto.OrderDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

  private final OrdersService orders;

  public OrdersController(OrdersService orders) {
    this.orders = orders;
  }

  @GetMapping("/mine")
  public List<OrderDtos.OrderSummary> mine() {
    return orders.myOrders();
  }

  @GetMapping("/{id}")
  public OrderDtos.OrderDetail get(@PathVariable Long id) {
    return orders.get(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderDtos.OrderDetail create(@Valid @RequestBody OrderDtos.CreateOrderRequest req) {
    return orders.createOrderFromCart(req);
  }
}
