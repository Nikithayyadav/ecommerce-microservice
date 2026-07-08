package com.ecommerce.service;

import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Product;
import com.ecommerce.model.Wishlist;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public List<ProductResponse> getWishlist(String customerId) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        return wishlistRepository.findByUserId(resolvedCustomerId).stream().map(w -> productService.toResponse(w.getProduct())).collect(Collectors.toList());
    }

    @Transactional
    public void addToWishlist(String customerId, Long productId) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        if (wishlistRepository.existsByUserIdAndProductId(resolvedCustomerId, productId)) {
            throw new BadRequestException("Already in wishlist");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        wishlistRepository.save(Wishlist.builder().userId(resolvedCustomerId).product(product).build());
    }

    @Transactional
    public void removeFromWishlist(String customerId, Long productId) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        wishlistRepository.deleteByUserIdAndProductId(resolvedCustomerId, productId);
    }
}
