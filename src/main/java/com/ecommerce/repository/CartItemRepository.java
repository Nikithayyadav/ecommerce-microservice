package com.ecommerce.repository;
import com.ecommerce.model.CartItem;import org.springframework.data.jpa.repository.*;import java.util.*;
public interface CartItemRepository extends JpaRepository<CartItem,Long> { List<CartItem> findByUserId(Long userId); Optional<CartItem> findByUserIdAndProductId(Long userId,Long productId); void deleteByUserId(Long userId); }
