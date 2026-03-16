package com.redearth.mysql.repo;

import com.redearth.mysql.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
}
