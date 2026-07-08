package com.ecommerce.dto.request;

import com.ecommerce.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank
    private String shippingAddress;

    private PaymentMethod paymentMethod = PaymentMethod.UPI;
}
