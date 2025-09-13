package com.iphonestore.productservice.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductSearchDto {
    private String model;
    private String storageCapacity;
    private String color;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minStock;
    private Boolean inStock;
}