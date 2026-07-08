package com.ecommerce.controller;
import com.ecommerce.dto.request.CategoryRequest;import com.ecommerce.dto.response.*;import com.ecommerce.service.CategoryService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import org.springframework.http.*;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/categories") @RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll(){return ResponseEntity.ok(ApiResponse.success("Categories fetched",categoryService.getAllCategories()));}
    @GetMapping("/{id}") public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id){return ResponseEntity.ok(ApiResponse.success("Category fetched",categoryService.getCategoryById(id)));}
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<CategoryResponse>> create(@Valid @RequestBody CategoryRequest req){return ResponseEntity.status(201).body(ApiResponse.success("Category created",categoryService.createCategory(req)));}
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<CategoryResponse>> update(@PathVariable Long id,@Valid @RequestBody CategoryRequest req){return ResponseEntity.ok(ApiResponse.success("Category updated",categoryService.updateCategory(id,req)));}
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id){categoryService.deleteCategory(id);return ResponseEntity.ok(ApiResponse.success("Category deleted"));}
}
