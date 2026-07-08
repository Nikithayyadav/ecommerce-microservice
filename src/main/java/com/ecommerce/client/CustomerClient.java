package com.ecommerce.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "crm-service",
        url = "${app.crm.service.url}"
)
public interface CustomerClient {

    @GetMapping("/api/customers/{customerId}")
    CustomerResponse getCustomer(@PathVariable("customerId") Long customerId);

    record CustomerResponse(Long id, String email, String name, String role) {
    }
}
