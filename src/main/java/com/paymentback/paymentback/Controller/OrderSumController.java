package com.paymentback.paymentback.Controller;

import com.paymentback.paymentback.Entity.OrderSumEntity;
import com.paymentback.paymentback.OrderDTO.OrderInfoDTO;
import com.paymentback.paymentback.services.OrderInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order-summary")
public class OrderSumController {

    private final OrderInfoService orderInfoService;

    public OrderSumController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    @GetMapping("/all")
    public List<OrderInfoDTO> getAllOrders() {
        return orderInfoService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderInfoDTO getOrderById(@PathVariable String id) {
        return orderInfoService.getOrderById(id);
    }

    @PostMapping("/order")
    public ResponseEntity<OrderSumEntity> createOrder(
            @RequestBody Map<String, Object> payload) {
        OrderSumEntity order = new OrderSumEntity();

        // Extract and set the order details from the payload
        order.setOrder_id((String) payload.get("order_id"));
        order.setAmount(new BigDecimal((String) payload.get("amount")));
        order.setCurrency((String) payload.get("currency"));
        order.setNIC((String) payload.get("NIC"));
        order.setFirst_name((String) payload.get("first_name"));
        order.setLast_name((String) payload.get("last_name"));
        order.setEmail((String) payload.get("email"));
        order.setPhone((String) payload.get("phone"));
        order.setAddress((String) payload.get("address"));
        order.setCity((String) payload.get("city"));
        order.setCountry((String) payload.get("country"));
        order.setStatus((String) payload.get("status"));
        order.setEvent_id((String) payload.get("event_id"));
        order.setUser_id((String) payload.get("user_id"));

        // Extract the ticket types from the payload
        List<String> ticketTypes = (List<String>) payload.get("ticketTypes");

        // Create the order with the ticket types
        OrderSumEntity createdOrder = orderInfoService.createOrder(order, ticketTypes);

        // Return the created order in the response
        return ResponseEntity.ok(createdOrder);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<OrderSumEntity> editOrder(
            @PathVariable String id,
            @RequestBody OrderSumEntity updatedOrder) {
        OrderSumEntity editedOrder = orderInfoService.updateOrderStatus(id, updatedOrder.getStatus());
        return ResponseEntity.ok(editedOrder);
    }

    @GetMapping("/success/{orderId}")
    public ResponseEntity<OrderInfoDTO> getOrderDetailsForSuccessPage(@PathVariable String orderId) {
        OrderInfoDTO order = orderInfoService.getOrderById(orderId);
        if (order.getStatus().equals("successful")) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<OrderSumEntity> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, String> statusRequest) {
        String status = statusRequest.get("status");
        OrderSumEntity updatedOrder = orderInfoService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/ticket-types/{orderId}")
    public ResponseEntity<List<String>> getTicketTypesByOrderId(@PathVariable String orderId) {
        OrderSumEntity order = orderInfoService.getOrderEntityById(orderId);

        if (order != null && "successful".equals(order.getStatus())) {
            List<String> ticketTypes = orderInfoService.getTicketTypesByOrderId(orderId);
            Collections.sort(ticketTypes); // Sort the ticket types
            return ResponseEntity.ok(ticketTypes);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
