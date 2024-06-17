package com.paymentback.paymentback.services;

import com.paymentback.paymentback.Entity.OrderSumEntity;
import com.paymentback.paymentback.Entity.TicketType;
import com.paymentback.paymentback.OrderDTO.OrderInfoDTO;
import com.paymentback.paymentback.Repository.OrderSumRepository;
import com.paymentback.paymentback.Repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderInfoService {

    private final OrderSumRepository orderSumRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Value("${merchant.secret}")
    private String merchantSecretPay;

    @Value("${merchant.id}")
    private String merchantId;

    @Autowired
    public OrderInfoService(OrderSumRepository orderSumRepository, TicketTypeRepository ticketTypeRepository) {
        this.orderSumRepository = orderSumRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public List<String> getTicketTypesByOrderId(String orderId) {
        List<String> ticketTypes = ticketTypeRepository.findTicketTypesByOrderId(orderId);
        Collections.sort(ticketTypes); // Sort the ticket types
        return ticketTypes;
    }

    public OrderSumEntity getOrderEntityById(String orderId) {
        Optional<OrderSumEntity> orderOptional = orderSumRepository.findById(orderId);
        return orderOptional.orElse(null);
    }

    public List<OrderInfoDTO> getAllOrders() {
        List<OrderSumEntity> orders = orderSumRepository.findAll();
        return orders.stream()
                .map(order -> {
                    String orderID = order.getOrder_id();
                    String currency = order.getCurrency();
                    DecimalFormat df = new DecimalFormat("0.00");
                    String amountFormatted = df.format(order.getAmount());
                    String hash = getMd5(merchantId + orderID + amountFormatted + currency + getMd5(merchantSecretPay));

                    return new OrderInfoDTO(order.getOrder_id(), order.getAmount(), order.getCurrency(), order.getNIC(), order.getFirst_name(), order.getLast_name(), order.getEmail(), order.getPhone(), order.getAddress(), order.getCity(), order.getCountry(), merchantId, order.getUser_id(), formatAmount(order.getAmount()), hash, order.getStatus(), order.getEvent_id());
                })
                .collect(Collectors.toList());
    }

    public OrderInfoDTO getOrderById(String id) {
        Optional<OrderSumEntity> orderOptional = orderSumRepository.findById(id);
        if (orderOptional.isPresent()) {
            OrderSumEntity order = orderOptional.get();
            String orderID = order.getOrder_id();
            String currency = order.getCurrency();
            DecimalFormat df = new DecimalFormat("0.00");
            String amountFormatted = df.format(order.getAmount());
            String hash = getMd5(merchantId + orderID + amountFormatted + currency + getMd5(merchantSecretPay));

            return new OrderInfoDTO(order.getOrder_id(), order.getAmount(), order.getCurrency(), order.getNIC(), order.getFirst_name(), order.getLast_name(), order.getEmail(), order.getPhone(), order.getAddress(), order.getCity(), order.getCountry(), merchantId, order.getUser_id(), formatAmount(order.getAmount()), hash, order.getStatus(), order.getEvent_id());
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    public OrderSumEntity createOrder(OrderSumEntity order, List<String> ticketTypes) {
        OrderSumEntity savedOrder = orderSumRepository.save(order);
        List<TicketType> tickets = ticketTypes.stream()
                .map(type -> new TicketType(type, savedOrder))
                .collect(Collectors.toList());
        ticketTypeRepository.saveAll(tickets);
        savedOrder.setTicketTypes(tickets);
        return savedOrder;
    }

    public OrderSumEntity updateOrderStatus(String orderId, String status) {
        OrderSumEntity order = orderSumRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setStatus(status);
        return orderSumRepository.save(order);
    }

    private String formatAmount(BigDecimal amount) {
        DecimalFormat df = new DecimalFormat("#.00");
        df.setGroupingUsed(false);
        return df.format(amount);
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
