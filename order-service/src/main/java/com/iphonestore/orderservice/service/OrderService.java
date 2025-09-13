package com.iphonestore.orderservice.service;

import com.iphonestore.orderservice.dto.OrderDto;
import com.iphonestore.orderservice.dto.OrderRequestDto;
import com.iphonestore.orderservice.dto.OrderResponseDto;
import com.iphonestore.orderservice.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequest);

    Optional<OrderDto> getOrderById(Long id);

    List<OrderDto> getAllOrders();

    List<OrderDto> getOrdersByCustomerEmail(String customerEmail);

    List<OrderDto> getOrdersByStatus(OrderStatus status);

    List<OrderDto> getOrdersWithFilters(OrderStatus status, String customerEmail,
            LocalDateTime fromDate, LocalDateTime toDate);

    boolean updateOrderStatus(Long orderId, OrderStatus newStatus);

    Map<String, Object> getSalesReport(LocalDateTime fromDate, LocalDateTime toDate);
}