package main.java.com.iphonestore.orderservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> items;
}
