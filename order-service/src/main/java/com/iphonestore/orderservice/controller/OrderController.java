package com.iphonestore.orderservice.controller;

import com.iphonestore.orderservice.dto.OrderDto;
import com.iphonestore.orderservice.dto.OrderRequestDto;
import com.iphonestore.orderservice.dto.OrderResponseDto;
import com.iphonestore.orderservice.enums.OrderStatus;
import com.iphonestore.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequest) {
        OrderResponseDto response = orderService.createOrder(orderRequest);

        if (response.getOrderId() != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        Optional<OrderDto> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        List<OrderDto> orders;

        if (status != null || customerEmail != null || fromDate != null || toDate != null) {
            orders = orderService.getOrdersWithFilters(status, customerEmail, fromDate, toDate);
        } else {
            orders = orderService.getAllOrders();
        }

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerEmail(@PathVariable String email) {
        List<OrderDto> orders = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {

        boolean updated = orderService.updateOrderStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Order status updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sales-report")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {

        Map<String, Object> report = orderService.getSalesReport(fromDate, toDate);
        return ResponseEntity.ok(report);
    }
}