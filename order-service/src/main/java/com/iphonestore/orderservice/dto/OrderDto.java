package com.iphonestore.orderservice.dto;

import com.iphonestore.orderservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private String customerName;
    private String customerEmail;
    private OrderStatus orderStatus;
    private BigDecimal totalAmount;
    private List<OrderItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
