package com.iphonestore.orderservice.service;

import com.iphonestore.orderservice.dto.*;
import com.iphonestore.orderservice.entity.Order;
import com.iphonestore.orderservice.entity.OrderItem;
import com.iphonestore.orderservice.enums.OrderStatus;
import com.iphonestore.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequest) {
        // Extract product IDs using Stream API
        List<Long> productIds = orderRequest.getItems().stream()
                .map(OrderItemDto::getProductId)
                .collect(Collectors.toList());

        // Check product availability
        List<ProductDto> availableProducts = productServiceClient.getAvailableProductsByIds(productIds);

        if (availableProducts.size() != productIds.size()) {
            return new OrderResponseDto(null, "Some products are not available or out of stock");
        }

        // Create product map for quick lookup using Stream API
        Map<Long, ProductDto> productMap = availableProducts.stream()
                .collect(Collectors.toMap(ProductDto::getId, product -> product));

        // Validate stock and calculate total using Java Streams
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto itemDto : orderRequest.getItems()) {
            ProductDto product = productMap.get(itemDto.getProductId());

            if (product == null || product.getStockQuantity() < itemDto.getQuantity()) {
                return new OrderResponseDto(null,
                        "Product " + itemDto.getProductId() + " is not available in requested quantity");
            }

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem orderItem = new OrderItem(
                    product.getId(),
                    product.getModel() + " " + product.getStorageCapacity() + " " + product.getColor(),
                    product.getPrice(),
                    itemDto.getQuantity());
            orderItem.calculateSubtotal();
            orderItems.add(orderItem);
        }

        // Create and save order
        Order order = new Order(orderRequest.getCustomerName(), orderRequest.getCustomerEmail(), totalAmount);
        order.setOrderStatus(OrderStatus.PENDING);

        orderItems.forEach(order::addItem);

        Order savedOrder = orderRepository.save(order);

        // Update product stock (simplified - in production, use saga pattern for
        // distributed transactions)
        updateProductStocks(orderRequest.getItems(), productMap);

        // Convert to response DTO using Stream API
        OrderResponseDto response = convertToResponseDto(savedOrder);
        response.setMessage("Order created successfully");

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderDto> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByCustomerEmail(String customerEmail) {
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(customerEmail)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByOrderStatusOrderByCreatedAtDesc(status)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersWithFilters(OrderStatus status, String customerEmail,
            LocalDateTime fromDate, LocalDateTime toDate) {
        String statusStr = status != null ? status.name() : null;

        return orderRepository.findOrdersWithFilters(statusStr, customerEmail, fromDate, toDate)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(newStatus);
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSalesReport(LocalDateTime fromDate, LocalDateTime toDate) {
        Map<String, Object> report = new HashMap<>();

        // Sales by status using native SQL
        List<Object[]> salesByStatus = orderRepository.getSalesReportByStatus(fromDate, toDate);
        Map<String, Map<String, Object>> statusReport = salesByStatus.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> {
                            Map<String, Object> statusData = new HashMap<>();
                            statusData.put("orderCount", row[1]);
                            statusData.put("totalRevenue", row[2]);
                            statusData.put("avgOrderValue", row[3]);
                            return statusData;
                        }));

        // Top selling products using native SQL
        List<Object[]> topProducts = orderRepository.getTopSellingProducts(fromDate, toDate);
        List<Map<String, Object>> topProductsList = topProducts.stream()
                .map(row -> {
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("productId", row[0]);
                    productData.put("productName", row[1]);
                    productData.put("totalQuantitySold", row[2]);
                    productData.put("totalRevenue", row[3]);
                    productData.put("orderCount", row[4]);
                    return productData;
                })
                .collect(Collectors.toList());

        // Daily statistics using native SQL
        List<Object[]> dailyStats = orderRepository.getDailyOrderStatistics(fromDate, toDate);
        List<Map<String, Object>> dailyStatsList = dailyStats.stream()
                .map(row -> {
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("date", row[0]);
                    dayData.put("dailyOrders", row[1]);
                    dayData.put("dailyRevenue", row[2]);
                    return dayData;
                })
                .collect(Collectors.toList());

        // Calculate totals using Stream API
        BigDecimal totalRevenue = statusReport.values().stream()
                .map(status -> (BigDecimal) status.get("totalRevenue"))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalOrders = statusReport.values().stream()
                .map(status -> (Long) status.get("orderCount"))
                .filter(Objects::nonNull)
                .reduce(0L, Long::sum);

        report.put("totalRevenue", totalRevenue);
        report.put("totalOrders", totalOrders);
        report.put("salesByStatus", statusReport);
        report.put("topSellingProducts", topProductsList);
        report.put("dailyStatistics", dailyStatsList);
        report.put("reportPeriod", Map.of("fromDate", fromDate, "toDate", toDate));

        return report;
    }

    private void updateProductStocks(List<OrderItemDto> items, Map<Long, ProductDto> productMap) {
        items.forEach(item -> {
            ProductDto product = productMap.get(item.getProductId());
            if (product != null) {
                int newStock = product.getStockQuantity() - item.getQuantity();
                productServiceClient.updateProductStock(item.getProductId(), newStock);
            }
        });
    }

    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        // Convert order items using Stream API
        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private OrderResponseDto convertToResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setCustomerName(order.getCustomerName());
        dto.setCustomerEmail(order.getCustomerEmail());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemDto> itemDtos = order.getItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);
        dto.setTotalItems(itemDtos.size());

        return dto;
    }

    private OrderItemDto convertItemToDto(OrderItem item) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setProductPrice(item.getProductPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}