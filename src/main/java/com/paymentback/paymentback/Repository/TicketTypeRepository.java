package com.paymentback.paymentback.Repository;

import com.paymentback.paymentback.Entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    @Query("SELECT t.ticketType FROM TicketType t WHERE t.orderSumEntity.order_id = :orderId")
    List<String> findTicketTypesByOrderId(@Param("orderId") String orderId);
}
