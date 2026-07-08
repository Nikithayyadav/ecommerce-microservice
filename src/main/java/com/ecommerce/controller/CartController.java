package com.ecommerce.controller;
import com.ecommerce.dto.request.CartItemRequest;import com.ecommerce.dto.response.*;import com.ecommerce.service.CartService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.http.*;import org.springframework.security.core.annotation.AuthenticationPrincipal;import org.springframework.security.core.userdetails.UserDetails;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/cart") @RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    @GetMapping public ResponseEntity<ApiResponse<List<CartResponse>>> getCart(@AuthenticationPrincipal UserDetails u){return ResponseEntity.ok(ApiResponse.success("Cart fetched",cartService.getCart(u.getUsername())));}
    @PostMapping("/add") public ResponseEntity<ApiResponse<CartResponse>> add(@AuthenticationPrincipal UserDetails u,@Valid @RequestBody CartItemRequest req){return ResponseEntity.status(201).body(ApiResponse.success("Item added",cartService.addItem(u.getUsername(),req)));}
    @PutMapping("/update/{id}") public ResponseEntity<ApiResponse<CartResponse>> update(@AuthenticationPrincipal UserDetails u,@PathVariable Long id,@Valid @RequestBody CartItemRequest req){return ResponseEntity.ok(ApiResponse.success("Cart updated",cartService.updateItem(u.getUsername(),id,req)));}
    @DeleteMapping("/remove/{id}") public ResponseEntity<ApiResponse<Void>> remove(@AuthenticationPrincipal UserDetails u,@PathVariable Long id){cartService.removeItem(u.getUsername(),id);return ResponseEntity.ok(ApiResponse.success("Item removed"));}
    @DeleteMapping("/clear") public ResponseEntity<ApiResponse<Void>> clear(@AuthenticationPrincipal UserDetails u){cartService.clearCart(u.getUsername());return ResponseEntity.ok(ApiResponse.success("Cart cleared"));}
}
