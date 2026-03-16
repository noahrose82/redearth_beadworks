package com.redearth.mysql.repo;

import com.redearth.mysql.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
  List<AddressEntity> findByUserId(Long userId);
}
