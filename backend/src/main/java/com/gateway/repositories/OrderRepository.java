package com.gateway.repositories;

import com.gateway.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByIdAndMerchantId(String id, String merchantId);
    List<Order> findByMerchantId(String merchantId);
}