
package com.redearth.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redearth.mongo.doc.ProductDoc;
import com.redearth.mongo.repo.ProductRepository;
import com.redearth.mysql.entity.OrderEntity;
import com.redearth.mysql.entity.OrderItemEntity;
import com.redearth.mysql.repo.OrderItemRepository;
import com.redearth.mysql.repo.OrderRepository;
import com.redearth.security.SecurityUtil;
import com.redearth.web.dto.OrderDtos;
import com.redearth.web.error.NotFoundException;

@Service
public class OrdersService {

    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final ProductRepository products;

    public OrdersService(OrderRepository orders, OrderItemRepository items, ProductRepository products) {
        this.orders = orders;
        this.items = items;
        this.products = products;
    }

    public List<OrderDtos.OrderSummary> myOrders() {
        Long userId = requireUser();
        return orders.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(o -> new OrderDtos.OrderSummary(
                        o.getId(),
                        o.getStatus(),
                        o.getTotalCents(),
                        o.getCreatedAt()
                ))
                .toList();
    }

    public OrderDtos.OrderDetail get(Long orderId) {
        Long userId = requireUser();

        OrderEntity order = orders.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getUserId() != null
                && !order.getUserId().equals(userId)
                && !SecurityUtil.hasRole("ADMIN")) {
            throw new NotFoundException("Order not found");
        }

        List<OrderItemEntity> list = items.findByOrderId(orderId);
        return OrderDtos.OrderDetail.from(order, list);
    }

    @Transactional
    public OrderDtos.OrderDetail createOrderFromCart(OrderDtos.CreateOrderRequest req) {
        Long userId = requireUser();

        if (req.items == null || req.items.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        int total = 0;
        OrderEntity order = new OrderEntity();

        order.setUserId(userId);
        order.setStatus("NEW");

        for (OrderDtos.CartItem ci : req.items) {
            ProductDoc p = products.findById(ci.productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product"));
            total += p.getPriceCents() * ci.quantity;
        }

        order.setTotalCents(total);
        OrderEntity saved = orders.save(order);

        for (OrderDtos.CartItem ci : req.items) {
            ProductDoc p = products.findById(ci.productId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid product"));

            OrderItemEntity item = new OrderItemEntity();
            item.setOrderId(saved.getId());
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setUnitPriceCents(p.getPriceCents());
            item.setQuantity(ci.quantity);
            items.save(item);
        }

        List<OrderItemEntity> list = items.findByOrderId(saved.getId());
        return OrderDtos.OrderDetail.from(saved, list);
    }

    private Long requireUser() {
        Long userId = SecurityUtil.currentUserId();
        if (userId == null) {
            throw new NotFoundException("Not authenticated");
        }
        return userId;
    }
}