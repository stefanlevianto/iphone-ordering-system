package com.iphonestore.productservice.controller;

import com.iphonestore.productservice.dto.ProductDto;
import com.iphonestore.productservice.dto.ProductSearchDto;
import com.iphonestore.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String storageCapacity,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Boolean inStock) {

        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setModel(model);
        searchDto.setStorageCapacity(storageCapacity);
        searchDto.setColor(color);
        searchDto.setMinPrice(minPrice);
        searchDto.setMaxPrice(maxPrice);
        searchDto.setMinStock(minStock);
        searchDto.setInStock(inStock);

        List<ProductDto> products = productService.searchProducts(searchDto);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStockProducts() {
        List<ProductDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        boolean updated = productService.updateStock(id, quantity);
        if (updated) {
            return ResponseEntity.ok("Stock updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/check-availability")
    public ResponseEntity<List<ProductDto>> checkAvailability(@RequestBody List<Long> productIds) {
        List<ProductDto> availableProducts = productService.getAvailableProductsByIds(productIds);
        return ResponseEntity.ok(availableProducts);
    }
}