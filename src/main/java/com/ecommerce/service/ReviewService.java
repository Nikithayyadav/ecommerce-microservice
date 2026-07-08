package com.ecommerce.service;

import com.ecommerce.dto.request.ReviewRequest;
import com.ecommerce.dto.response.ReviewResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    private ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .userId(review.getUserId())
                .userName(review.getUserName())
                .productId(review.getProduct().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

    public Page<ReviewResponse> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable).map(this::toResponse);
    }

    @Transactional
    public ReviewResponse addReview(String customerId, Long productId, ReviewRequest req) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (reviewRepository.existsByUserIdAndProductId(resolvedCustomerId, productId)) {
            throw new BadRequestException("You already reviewed this product");
        }
        return toResponse(reviewRepository.save(Review.builder().userId(resolvedCustomerId).userName("Customer " + resolvedCustomerId).product(product).rating(req.getRating()).comment(req.getComment()).build()));
    }

    @Transactional
    public ReviewResponse updateReview(String customerId, Long reviewId, ReviewRequest req) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your review");
        }
        review.setRating(req.getRating());
        review.setComment(req.getComment());
        return toResponse(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(String customerId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (!review.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your review");
        }
        reviewRepository.delete(review);
    }
}
