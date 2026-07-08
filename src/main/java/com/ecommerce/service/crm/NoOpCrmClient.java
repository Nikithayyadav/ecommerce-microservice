package com.ecommerce.service.crm;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(CrmClient.class)
public class NoOpCrmClient implements CrmClient {

    @Override
    public void syncCustomer(Long customerId, String name, String email) {
        // Placeholder for future CRM integration.
    }
}
