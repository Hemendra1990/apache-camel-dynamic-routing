package com.hemendra.camel.learncamel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;
import com.hemendra.camel.learncamel.config.ClientConfigs;
import com.hemendra.camel.learncamel.service.TenantResolver;

@Component
public class MultitenancyCamelRouteBuilder extends RouteBuilder {
    
    private final TenantResolver tenantResolver;

    public MultitenancyCamelRouteBuilder(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public void configure() throws Exception {
        ClientConfigs clientConfig = this.tenantResolver.getClientConfig();
        // Configure the REST DSL
    }
    
}
