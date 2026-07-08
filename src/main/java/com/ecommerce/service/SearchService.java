package com.ecommerce.service;
import com.ecommerce.dto.response.ProductResponse;import com.ecommerce.repository.ProductRepository;import lombok.RequiredArgsConstructor;import org.springframework.data.domain.*;import org.springframework.stereotype.Service;import java.math.BigDecimal;
@Service @RequiredArgsConstructor
public class SearchService {
    private final ProductRepository productRepository;private final ProductService productService;
    public Page<ProductResponse> searchProducts(String q,Long categoryId,BigDecimal minPrice,BigDecimal maxPrice,Pageable pageable){return productRepository.search(q,categoryId,minPrice,maxPrice,pageable).map(productService::toResponse);}
}
