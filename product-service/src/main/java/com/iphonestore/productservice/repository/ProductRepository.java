package com.iphonestore.productservice.repository;

import com.iphonestore.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT p.* FROM products p
            WHERE (:model IS NULL OR LOWER(p.model) LIKE LOWER(CONCAT('%', :model, '%')))
            AND (:storageCapacity IS NULL OR LOWER(p.storage_capacity) = LOWER(:storageCapacity))
            AND (:color IS NULL OR LOWER(p.color) LIKE LOWER(CONCAT('%', :color, '%')))
            AND (:minPrice IS NULL OR p.price >= :minPrice)
            AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            AND (:minStock IS NULL OR p.stock_quantity >= :minStock)
            AND (:inStock IS NULL OR (:inStock = true AND p.stock_quantity > 0) OR (:inStock = false))
            ORDER BY p.model, p.storage_capacity, p.color
            """, nativeQuery = true)
    List<Product> searchProducts(
            @Param("model") String model,
            @Param("storageCapacity") String storageCapacity,
            @Param("color") String color,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("minStock") Integer minStock,
            @Param("inStock") Boolean inStock);

    @Query(value = """
            SELECT p.* FROM products p
            WHERE p.stock_quantity < 10
            ORDER BY p.stock_quantity ASC, p.model
            """, nativeQuery = true)
    List<Product> findLowStockProducts();

    @Query(value = """
            SELECT p.* FROM products p
            WHERE p.stock_quantity > 0
            AND p.id IN :productIds
            ORDER BY p.model
            """, nativeQuery = true)
    List<Product> findAvailableProductsByIds(@Param("productIds") List<Long> productIds);

    @Query(value = """
            SELECT
                p.model,
                COUNT(*) as model_count,
                AVG(p.price) as avg_price,
                SUM(p.stock_quantity) as total_stock
            FROM products p
            GROUP BY p.model
            ORDER BY model_count DESC
            """, nativeQuery = true)
    List<Object[]> getProductStatsByModel();
}