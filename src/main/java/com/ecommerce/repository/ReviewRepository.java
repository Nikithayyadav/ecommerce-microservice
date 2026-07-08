package com.ecommerce.repository;
import com.ecommerce.model.Review;import org.springframework.data.domain.*;import org.springframework.data.jpa.repository.*;import org.springframework.data.repository.query.Param;
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id=:pid") Double getAverageRatingByProductId(@Param("pid") Long productId);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.product.id=:pid") Long getReviewCountByProductId(@Param("pid") Long productId);
}
