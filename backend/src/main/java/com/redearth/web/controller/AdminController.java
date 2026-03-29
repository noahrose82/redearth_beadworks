package com.redearth.web.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redearth.security.SecurityUtil;
import com.redearth.service.AdminService;
import com.redearth.web.dto.AdminDtos;
import com.redearth.web.error.NotFoundException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminService admin;

  public AdminController(AdminService admin) {
    this.admin = admin;
  }

  @GetMapping("/orders")
  public List<AdminDtos.AdminOrderSummary> listOrders() {
    if (!SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Not authorized");
    }
    return admin.listOrders();
  }

  @PutMapping("/orders/{id}")
  public AdminDtos.AdminOrderSummary updateOrderStatus(
      @PathVariable Long id,
      @RequestBody AdminDtos.UpdateOrderStatusRequest req
  ) {
    if (!SecurityUtil.hasRole("ADMIN")) {
      throw new NotFoundException("Not authorized");
    }
    return admin.updateOrderStatus(id, req.status);
  }
}