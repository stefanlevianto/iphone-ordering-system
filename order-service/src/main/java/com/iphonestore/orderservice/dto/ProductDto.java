package main.java.com.iphonestore.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String model;
    private String storageCapacity;
    private String color;
    private BigDecimal price;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}