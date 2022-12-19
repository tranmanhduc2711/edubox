package com.example.edubox.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig{
    @Value("${app.client.base-path}")
    String clientBasePath;

    @Bean
    public String getClientBasePath(){
        return clientBasePath;
    }
}
