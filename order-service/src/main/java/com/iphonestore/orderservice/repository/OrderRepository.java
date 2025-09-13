package com.iphonestore.orderservice.repository;

import com.iphonestore.orderservice.entity.Order;
import com.iphonestore.orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);

    List<Order> findByOrderStatusOrderByCreatedAtDesc(OrderStatus orderStatus);

    @Query(value = """
            SELECT o.* FROM orders o
            WHERE (:status IS NULL OR o.order_status = :status)
            AND (:customerEmail IS NULL OR LOWER(o.customer_email) LIKE LOWER(CONCAT('%', :customerEmail, '%')))
            AND (:fromDate IS NULL OR o.created_at >= :fromDate)
            AND (:toDate IS NULL OR o.created_at <= :toDate)
            ORDER BY o.created_at DESC
            """, nativeQuery = true)
    List<Order> findOrdersWithFilters(
            @Param("status") String status,
            @Param("customerEmail") String customerEmail,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query(value = """
            SELECT
                o.order_status,
                COUNT(*) as order_count,
                SUM(o.total_amount) as total_revenue,
                AVG(o.total_amount) as avg_order_value
            FROM orders o
            WHERE o.created_at >= :fromDate
            AND o.created_at <= :toDate
            GROUP BY o.order_status
            ORDER BY total_revenue DESC
            """, nativeQuery = true)
    List<Object[]> getSalesReportByStatus(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query(value = """
            SELECT
                oi.product_id,
                oi.product_name,
                SUM(oi.quantity) as total_quantity_sold,
                SUM(oi.subtotal) as total_revenue,
                COUNT(DISTINCT o.id) as order_count
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            WHERE o.order_status IN ('CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED')
            AND o.created_at >= :fromDate
            AND o.created_at <= :toDate
            GROUP BY oi.product_id, oi.product_name
            ORDER BY total_quantity_sold DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Object[]> getTopSellingProducts(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    @Query(value = """
            SELECT
                DATE(o.created_at) as order_date,
                COUNT(*) as daily_orders,
                SUM(o.total_amount) as daily_revenue
            FROM orders o
            WHERE o.created_at >= :fromDate
            AND o.created_at <= :toDate
            GROUP BY DATE(o.created_at)
            ORDER BY order_date DESC
            """, nativeQuery = true)
    List<Object[]> getDailyOrderStatistics(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
}