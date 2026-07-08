package com.ecommerce.controller;
import com.ecommerce.dto.request.ReviewRequest;import com.ecommerce.dto.response.*;import com.ecommerce.service.ReviewService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/reviews") @RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping("/product/{productId}") public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getByProduct(@PathVariable Long productId,@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="10")int size){return ResponseEntity.ok(ApiResponse.success("Reviews fetched",reviewService.getProductReviews(productId,PageRequest.of(page,size))));}
    @PostMapping("/product/{productId}") public ResponseEntity<ApiResponse<ReviewResponse>> add(@AuthenticationPrincipal UserDetails u,@PathVariable Long productId,@Valid @RequestBody ReviewRequest req){return ResponseEntity.status(201).body(ApiResponse.success("Review added",reviewService.addReview(u.getUsername(),productId,req)));}
    @PutMapping("/{reviewId}") public ResponseEntity<ApiResponse<ReviewResponse>> update(@AuthenticationPrincipal UserDetails u,@PathVariable Long reviewId,@Valid @RequestBody ReviewRequest req){return ResponseEntity.ok(ApiResponse.success("Review updated",reviewService.updateReview(u.getUsername(),reviewId,req)));}
    @DeleteMapping("/{reviewId}") public ResponseEntity<ApiResponse<Void>> delete(@AuthenticationPrincipal UserDetails u,@PathVariable Long reviewId){reviewService.deleteReview(u.getUsername(),reviewId);return ResponseEntity.ok(ApiResponse.success("Review deleted"));}
}
