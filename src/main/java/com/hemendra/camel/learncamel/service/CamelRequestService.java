package com.hemendra.camel.learncamel.service;


import org.springframework.stereotype.Service;

@Service
public class CamelRequestService {

    
    private TenantResolver tenantResolver;
    
    public CamelRequestService(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    
}
