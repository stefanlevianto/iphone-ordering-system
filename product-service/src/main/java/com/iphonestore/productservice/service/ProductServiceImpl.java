package com.iphonestore.productservice.service;

import com.iphonestore.productservice.dto.ProductDto;
import com.iphonestore.productservice.dto.ProductSearchDto;
import com.iphonestore.productservice.entity.Product;
import com.iphonestore.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(ProductSearchDto searchDto) {
        List<Product> products = productRepository.searchProducts(
                searchDto.getModel(),
                searchDto.getStorageCapacity(),
                searchDto.getColor(),
                searchDto.getMinPrice(),
                searchDto.getMaxPrice(),
                searchDto.getMinStock(),
                searchDto.getInStock());

        // Using Java Streams for additional filtering and processing
        return products.stream()
                .filter(product -> product.getStockQuantity() != null)
                .map(this::convertToDto)
                .sorted((p1, p2) -> {
                    // Sort by model first, then by price
                    int modelCompare = p1.getModel().compareTo(p2.getModel());
                    if (modelCompare != 0)
                        return modelCompare;
                    return p1.getPrice().compareTo(p2.getPrice());
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getLowStockProducts() {
        return productRepository.findLowStockProducts()
                .stream()
                .filter(product -> product.getStockQuantity() < 10)
                .map(this::convertToDto)
                .collect(Collectors.groupingBy(ProductDto::getModel))
                .values()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateStock(Long productId, Integer newQuantity) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> getAvailableProductsByIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }

        return productRepository.findAvailableProductsByIds(productIds)
                .stream()
                .filter(product -> product.getStockQuantity() > 0)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ProductDto convertToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getModel(),
                product.getStorageCapacity(),
                product.getColor(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }
}