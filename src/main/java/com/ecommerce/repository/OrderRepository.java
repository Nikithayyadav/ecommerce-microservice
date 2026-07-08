package com.ecommerce.repository;
import com.ecommerce.model.Order;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.*;import java.math.BigDecimal;
public interface OrderRepository extends JpaRepository<Order,Long> {
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    @Query("SELECT COUNT(o) FROM Order o") Long countAllOrders();
    @Query("SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o WHERE o.status='DELIVERED'") BigDecimal sumDeliveredRevenue();
}
