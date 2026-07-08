package com.ecommerce.dto.request;
import jakarta.validation.constraints.*;import lombok.Data;import java.math.BigDecimal;import java.util.List;
@Data public class ProductRequest { @NotBlank private String name; private String description; @NotNull @Positive private BigDecimal price; private BigDecimal discountPrice; @NotNull @Min(0) private Integer stock; private Long categoryId; private List<String> imageUrls; }
