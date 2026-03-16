package com.redearth.mysql.repo;

import com.redearth.mysql.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
  Optional<PaymentEntity> findByProviderIntentId(String providerIntentId);
}
