package com.ecommerce.dto.response;
import lombok.*;import java.math.BigDecimal;import java.time.LocalDateTime;import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderResponse {
    private Long id; private Long userId; private String userName; private BigDecimal totalAmount; private String status; private String shippingAddress; private List<OrderItemResponse> orderItems; private LocalDateTime createdAt;
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class OrderItemResponse { private Long productId; private String productName; private Integer quantity; private BigDecimal unitPrice; private String imageUrl; }
}
