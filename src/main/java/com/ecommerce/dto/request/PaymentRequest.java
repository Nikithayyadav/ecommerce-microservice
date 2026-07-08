package com.ecommerce.dto.request;

import com.ecommerce.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {

    private Long orderId;

    private Long customerId;

    private Double amount;

    private PaymentMethod paymentMethod;
}