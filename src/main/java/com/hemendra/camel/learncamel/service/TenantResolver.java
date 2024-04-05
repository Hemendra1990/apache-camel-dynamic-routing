package com.hemendra.camel.learncamel.service;

import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hemendra.camel.learncamel.config.ClientConfigs;

@Component
public class TenantResolver {

    private final ResourceLoader resourceLoader;
    private ClientConfigs clientConfig;

    public TenantResolver(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Resource resource = this.resourceLoader.getResource("classpath:client-config.json");
            this.clientConfig = objectMapper.readValue(resource.getInputStream(), ClientConfigs.class);
            System.out.println(clientConfig.getClientConfigs().get(0).getClientName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientConfigs getClientConfig() {
        return clientConfig;
    }
}
