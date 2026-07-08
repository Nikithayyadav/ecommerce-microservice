package com.ecommerce.service;

import com.ecommerce.dto.request.PaymentRequest;
import com.ecommerce.dto.response.PaymentResponse;
import com.ecommerce.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public PaymentServiceClient(RestTemplate restTemplate,
                                @Value("${app.payment-service.url:http://localhost:8082/payments/process}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            return restTemplate.postForObject(paymentServiceUrl, request, PaymentResponse.class);
        } catch (HttpStatusCodeException ex) {
            throw new BadRequestException("Payment service returned an error: " + ex.getStatusCode());
        } catch (ResourceAccessException ex) {
            throw new BadRequestException("Payment service is unavailable or timed out");
        } catch (RestClientException ex) {
            throw new BadRequestException("Unable to reach payment service");
        }
    }
}