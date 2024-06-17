package com.paymentback.paymentback.Repository;

import com.paymentback.paymentback.Entity.OrderSumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSumRepository extends JpaRepository<OrderSumEntity, String> {
}
