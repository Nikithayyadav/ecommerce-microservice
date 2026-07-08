package com.ecommerce.dto.response;
import lombok.*;import java.math.BigDecimal;import java.time.LocalDateTime;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CartResponse { private Long id; private Long productId; private String productName; private BigDecimal price; private BigDecimal discountPrice; private Integer quantity; private String imageUrl; private LocalDateTime addedAt; }
