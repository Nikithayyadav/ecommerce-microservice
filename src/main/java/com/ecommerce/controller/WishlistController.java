package com.ecommerce.controller;
import com.ecommerce.dto.response.*;import com.ecommerce.service.WishlistService;import lombok.RequiredArgsConstructor;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/wishlist") @RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    @GetMapping public ResponseEntity<ApiResponse<List<ProductResponse>>> getWishlist(@AuthenticationPrincipal UserDetails u){return ResponseEntity.ok(ApiResponse.success("Wishlist fetched",wishlistService.getWishlist(u.getUsername())));}
    @PostMapping("/add/{productId}") public ResponseEntity<ApiResponse<Void>> add(@AuthenticationPrincipal UserDetails u,@PathVariable Long productId){wishlistService.addToWishlist(u.getUsername(),productId);return ResponseEntity.ok(ApiResponse.success("Added to wishlist"));}
    @DeleteMapping("/remove/{productId}") public ResponseEntity<ApiResponse<Void>> remove(@AuthenticationPrincipal UserDetails u,@PathVariable Long productId){wishlistService.removeFromWishlist(u.getUsername(),productId);return ResponseEntity.ok(ApiResponse.success("Removed from wishlist"));}
}
