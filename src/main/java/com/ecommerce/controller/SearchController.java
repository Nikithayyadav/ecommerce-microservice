package com.ecommerce.controller;
import com.ecommerce.dto.response.*;import com.ecommerce.service.SearchService;import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.math.BigDecimal;
@RestController @RequestMapping("/api/search") @RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    @GetMapping("/products") public ResponseEntity<ApiResponse<Page<ProductResponse>>> search(@RequestParam(required=false)String q,@RequestParam(required=false)Long category,@RequestParam(required=false)BigDecimal minPrice,@RequestParam(required=false)BigDecimal maxPrice,@RequestParam(defaultValue="createdAt")String sortBy,@RequestParam(defaultValue="desc")String direction,@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="12")int size){Sort sort=direction.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();return ResponseEntity.ok(ApiResponse.success("Search results",searchService.searchProducts(q,category,minPrice,maxPrice,PageRequest.of(page,size,sort))));}
}
