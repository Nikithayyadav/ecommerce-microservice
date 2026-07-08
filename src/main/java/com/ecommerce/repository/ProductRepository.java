package com.ecommerce.repository;
import com.ecommerce.model.Product;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;import java.math.BigDecimal;
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%',:q,'%'))) AND (:categoryId IS NULL OR p.category.id = :categoryId) AND (:minPrice IS NULL OR p.price >= :minPrice) AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> search(@Param("q") String q, @Param("categoryId") Long categoryId, @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
}
