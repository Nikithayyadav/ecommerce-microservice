package com.ecommerce.service;

import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.request.PaymentRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.dto.response.PaymentResponse;
import com.ecommerce.enums.PaymentMethod;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.exception.UnauthorizedException;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;
import com.ecommerce.model.ProductImage;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final PaymentServiceClient paymentServiceClient;

    private OrderResponse toResponse(Order o) {
        List<OrderResponse.OrderItemResponse> items = o.getOrderItems().stream().map(i -> {
            String img = i.getProduct().getImages().stream().filter(ProductImage::getIsPrimary).findFirst().map(ProductImage::getImageUrl).orElse(null);
            return OrderResponse.OrderItemResponse.builder()
                    .productId(i.getProduct().getId())
                    .productName(i.getProduct().getName())
                    .quantity(i.getQuantity())
                    .unitPrice(i.getUnitPrice())
                    .imageUrl(img)
                    .build();
        }).collect(Collectors.toList());
        return OrderResponse.builder()
                .id(o.getId())
                .userId(o.getUserId())
                .userName(o.getUserName())
                .totalAmount(o.getTotalAmount())
                .status(o.getStatus().name())
                .shippingAddress(o.getShippingAddress())
                .orderItems(items)
                .createdAt(o.getCreatedAt())
                .build();
    }

    @Transactional
    public OrderResponse placeOrder(String customerId, OrderRequest req) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        List<CartItem> cart = cartItemRepository.findByUserId(resolvedCustomerId);
        if (cart.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        BigDecimal total = cart.stream().map(i -> {
            Product p = i.getProduct();
            if (p.getStock() < i.getQuantity()) {
                throw new BadRequestException("Insufficient stock for: " + p.getName());
            }
            BigDecimal price = p.getDiscountPrice() != null ? p.getDiscountPrice() : p.getPrice();
            return price.multiply(BigDecimal.valueOf(i.getQuantity()));
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder().userId(resolvedCustomerId).userName("Customer " + resolvedCustomerId).totalAmount(total).shippingAddress(req.getShippingAddress()).build();
        cart.forEach(i -> {
            Product p = i.getProduct();
            BigDecimal price = p.getDiscountPrice() != null ? p.getDiscountPrice() : p.getPrice();
            order.getOrderItems().add(OrderItem.builder().order(order).product(p).quantity(i.getQuantity()).unitPrice(price).build());
            p.setStock(p.getStock() - i.getQuantity());
            productRepository.save(p);
        });

        orderRepository.save(order);
        cartItemRepository.deleteByUserId(resolvedCustomerId);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setCustomerId(resolvedCustomerId);
        paymentRequest.setAmount(total.doubleValue());
        paymentRequest.setPaymentMethod(req.getPaymentMethod() != null ? req.getPaymentMethod() : PaymentMethod.UPI);

        try {
            PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest);
            String normalizedStatus = normalizePaymentStatus(paymentResponse.getPaymentStatus());
            order.setStatus("PAID".equals(normalizedStatus) ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
        } catch (RuntimeException ex) {
            log.warn("Payment processing failed for order {}", order.getId(), ex);
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        return toResponse(orderRepository.save(order));
    }

    private String normalizePaymentStatus(String status) {
        if (status == null || status.isBlank()) {
            return "PENDING";
        }
        return switch (status.trim().toUpperCase()) {
            case "SUCCESS", "PAID" -> "PAID";
            case "FAILED", "PAYMENT_FAILED" -> "PAYMENT_FAILED";
            default -> status.trim().toUpperCase();
        };
    }

    public Page<OrderResponse> getUserOrders(String customerId, Pageable p) {
        Long resolvedCustomerId = Long.parseLong(customerId);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(resolvedCustomerId, p).map(this::toResponse);
    }

    public OrderResponse getOrderById(String customerId, Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!o.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your order");
        }
        return toResponse(o);
    }

    @Transactional
    public OrderResponse cancelOrder(String customerId, Long id) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!o.getUserId().equals(Long.parseLong(customerId))) {
            throw new UnauthorizedException("Not your order");
        }
        if (o.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot cancel delivered order");
        }
        o.getOrderItems().forEach(i -> {
            i.getProduct().setStock(i.getProduct().getStock() + i.getQuantity());
            productRepository.save(i.getProduct());
        });
        o.setStatus(OrderStatus.CANCELLED);
        return toResponse(orderRepository.save(o));
    }

    public Page<OrderResponse> getAllOrders(Pageable p) {
        return orderRepository.findAll(p).map(this::toResponse);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {
        Order o = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        o.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return toResponse(orderRepository.save(o));
    }
}
