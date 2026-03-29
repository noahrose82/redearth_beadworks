package com.redearth.mysql.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.redearth.mysql.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}