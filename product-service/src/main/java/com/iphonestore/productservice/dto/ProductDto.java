package com.iphonestore.productservice.dto;

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

    // Constructors
    public ProductDto() {
    }

    public ProductDto(Long id, String model, String storageCapacity, String color,
            BigDecimal price, Integer stockQuantity, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.model = model;
        this.storageCapacity = storageCapacity;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}