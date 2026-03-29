package com.redearth.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.repo.OrderRepository;
import com.redearth.web.dto.AdminDtos;
import com.redearth.web.error.NotFoundException;

@Service
public class AdminService {

  private final OrderRepository orders;

  public AdminService(OrderRepository orders) {
    this.orders = orders;
  }

  public List<AdminDtos.AdminOrderSummary> listOrders() {
    return orders.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
        .stream()
        .map(o -> new AdminDtos.AdminOrderSummary(
            o.getId(),
            o.getStatus(),
            o.getTotalCents(),
            o.getCreatedAt()
        ))
        .toList();
  }

  public AdminDtos.AdminOrderSummary updateOrderStatus(Long id, String status) {
    OrderEntity order = orders.findById(id)
        .orElseThrow(() -> new NotFoundException("Order not found"));

    order.setStatus(status);
    OrderEntity saved = orders.save(order);

    return new AdminDtos.AdminOrderSummary(
        saved.getId(),
        saved.getStatus(),
        saved.getTotalCents(),
        saved.getCreatedAt()
    );
  }
}