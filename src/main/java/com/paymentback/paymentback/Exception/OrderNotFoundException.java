package com.paymentback.paymentback.Exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id){
        super("Order not found id"+id);
    }

}
