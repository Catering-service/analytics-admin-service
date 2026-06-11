package com.catering.analyticsadmin.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign configuration that adds internal auth headers
 * for service-to-service communication.
 * The gateway forwards X-Roles and X-User headers;
 * this interceptor adds them directly for internal calls.
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor internalAuthInterceptor() {
        return (RequestTemplate template) -> {
            template.header("X-Roles", "[ROLE_ADMIN]");
            template.header("X-User", "analytics-admin-service");
        };
    }
}
