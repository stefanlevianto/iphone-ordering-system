package com.iphonestore.orderservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotBlank
    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "product_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal productPrice;

    @Min(1)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    // Constructors
    public OrderItem() {
    }

    public OrderItem(Long productId, String productName, BigDecimal productPrice, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public void calculateSubtotal() {
        if (productPrice != null && quantity != null) {
            this.subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}