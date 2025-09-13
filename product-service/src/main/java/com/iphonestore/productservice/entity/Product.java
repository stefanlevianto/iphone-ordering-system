package com.iphonestore.productservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @NotBlank
    @Column(name = "storage_capacity", nullable = false, length = 20)
    private String storageCapacity;

    @NotBlank
    @Column(name = "color", nullable = false, length = 30)
    private String color;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "price", precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Min(0)
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Product() {
    }

    public Product(String model, String storageCapacity, String color, BigDecimal price, Integer stockQuantity) {
        this.model = model;
        this.storageCapacity = storageCapacity;
        this.color = color;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}