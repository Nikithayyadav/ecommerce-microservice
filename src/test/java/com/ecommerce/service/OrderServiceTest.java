package com.ecommerce.service;

import com.ecommerce.dto.request.OrderRequest;
import com.ecommerce.dto.request.PaymentRequest;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.dto.response.PaymentResponse;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository orderRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private PaymentServiceClient paymentServiceClient;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        cartItemRepository = mock(CartItemRepository.class);
        productRepository = mock(ProductRepository.class);
        paymentServiceClient = mock(PaymentServiceClient.class);
        orderService = new OrderService(orderRepository, cartItemRepository, productRepository, paymentServiceClient);
    }

    @Test
    void placeOrderShouldProcessPaymentAndUpdateOrderStatus() {
        Product product = Product.builder().id(10L).name("Laptop").price(new BigDecimal("1000.00")).discountPrice(new BigDecimal("900.00")).stock(5).build();
        CartItem cartItem = CartItem.builder().userId(1L).product(product).quantity(1).build();
        OrderRequest request = new OrderRequest();
        request.setShippingAddress("123 Main St");

        when(cartItemRepository.findByUserId(1L)).thenReturn(List.of(cartItem));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentStatus("SUCCESS");
        paymentResponse.setMessage("Payment successful");
        when(paymentServiceClient.processPayment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        OrderResponse response = orderService.placeOrder("1", request);

        ArgumentCaptor<PaymentRequest> captor = ArgumentCaptor.forClass(PaymentRequest.class);
        verify(paymentServiceClient).processPayment(captor.capture());
        assertEquals(1L, captor.getValue().getOrderId());
        assertEquals(1L, captor.getValue().getCustomerId());
        assertEquals(900.00, captor.getValue().getAmount());
        assertEquals(OrderStatus.PAID, response.getStatus() != null ? OrderStatus.valueOf(response.getStatus()) : null);
    }
}
