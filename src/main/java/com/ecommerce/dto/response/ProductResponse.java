package com.ecommerce.dto.response;
import lombok.*;import java.math.BigDecimal;import java.time.LocalDateTime;import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponse { private Long id; private String name; private String description; private BigDecimal price; private BigDecimal discountPrice; private Integer stock; private Long categoryId; private String categoryName; private List<String> imageUrls; private Double averageRating; private Long reviewCount; private LocalDateTime createdAt; }
