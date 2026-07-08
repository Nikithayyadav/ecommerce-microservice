package com.ecommerce.service.crm;

public interface CrmClient {
    void syncCustomer(Long customerId, String name, String email);
}
