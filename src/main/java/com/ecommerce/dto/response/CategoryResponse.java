package com.ecommerce.dto.response;
import lombok.*;import java.time.LocalDateTime;import java.util.List;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CategoryResponse { private Long id; private String name; private String slug; private String description; private Long parentCategoryId; private String parentCategoryName; private List<CategoryResponse> subCategories; private LocalDateTime createdAt; }
