package main.java.com.iphonestore.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDto {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String productName;
    private BigDecimal productPrice;
    private BigDecimal subtotal;
}
