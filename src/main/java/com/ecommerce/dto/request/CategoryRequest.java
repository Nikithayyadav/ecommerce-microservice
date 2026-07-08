package com.ecommerce.dto.request;
import jakarta.validation.constraints.*;import lombok.Data;
@Data public class CategoryRequest { @NotBlank private String name; private String description; private Long parentCategoryId; }
