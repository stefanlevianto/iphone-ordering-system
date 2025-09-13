package com.iphonestore.productservice.service;

import com.iphonestore.productservice.dto.ProductDto;
import com.iphonestore.productservice.dto.ProductSearchDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> getAllProducts();

    Optional<ProductDto> getProductById(Long id);

    List<ProductDto> searchProducts(ProductSearchDto searchDto);

    List<ProductDto> getLowStockProducts();

    boolean updateStock(Long productId, Integer newQuantity);

    List<ProductDto> getAvailableProductsByIds(List<Long> productIds);
}