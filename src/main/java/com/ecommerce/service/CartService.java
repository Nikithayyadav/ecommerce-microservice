package com.ecommerce.service;

import com.ecommerce.dto.request.CartItemRequest;
import com.ecommerce.dto.response.CartResponse;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.ProductImage;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    private CartResponse toResponse(CartItem item) {
        String img = item.getProduct().getImages().stream().filter(ProductImage::getIsPrimary).findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(item.getProduct().getImages().isEmpty() ? null : item.getProduct().getImages().get(0).getImageUrl());
        return CartResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .discountPrice(item.getProduct().getDiscountPrice())
                .quantity(item.getQuantity())
                .imageUrl(img)
                .addedAt(item.getAddedAt())
                .build();
    }

    public List<CartResponse> getCart(String customerId) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        return cartItemRepository.findByUserId(resolvedCustomerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public CartResponse addItem(String customerId, CartItemRequest req) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        Product product = productRepository.findById(req.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getStock() < req.getQuantity()) {
            throw new BadRequestException("Insufficient stock");
        }
        CartItem item = cartItemRepository.findByUserIdAndProductId(resolvedCustomerId, product.getId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + req.getQuantity());
                    return existing;
                })
                .orElse(CartItem.builder().userId(resolvedCustomerId).product(product).quantity(req.getQuantity()).build());
        return toResponse(cartItemRepository.save(item));
    }

    @Transactional
    public CartResponse updateItem(String customerId, Long itemId, CartItemRequest req) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your cart item");
        }
        item.setQuantity(req.getQuantity());
        return toResponse(cartItemRepository.save(item));
    }

    @Transactional
    public void removeItem(String customerId, Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (!item.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your cart item");
        }
        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(String customerId) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        cartItemRepository.deleteByUserId(resolvedCustomerId);
    }
}
