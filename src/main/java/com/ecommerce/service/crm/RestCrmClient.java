package com.ecommerce.service.crm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
@RequiredArgsConstructor
public class RestCrmClient implements CrmClient {

    private final RestTemplate restTemplate;

    @Value("${app.crm.service.url:http://localhost:8083}")
    private String crmBaseUrl;

    @Override
    public void syncCustomer(Long customerId, String name, String email) {
        // Placeholder for future CRM API integration.
    }
}
