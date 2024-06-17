package com.paymentback.paymentback.Entity;


import jakarta.persistence.*;

@Entity
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketType;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderSumEntity orderSumEntity;

    public TicketType() {
    }

    public TicketType(String ticketType, OrderSumEntity orderSumEntity) {
        this.ticketType = ticketType;
        this.orderSumEntity = orderSumEntity;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public OrderSumEntity getOrderSumEntity() {
        return orderSumEntity;
    }

    public void setOrderSumEntity(OrderSumEntity orderSumEntity) {
        this.orderSumEntity = orderSumEntity;
    }
}
