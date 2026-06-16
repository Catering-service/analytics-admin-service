package com.catering.analyticsadmin.config;

import com.catering.analyticsadmin.security.HeaderAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final HeaderAuthFilter headerAuthFilter;

    public SecurityConfig(HeaderAuthFilter headerAuthFilter) {
        this.headerAuthFilter = headerAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(headerAuthFilter, AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS
                        .requestMatchers(
                                "/actuator/health",
                                "/actuator/info",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/api/administrators/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin-logs/**").hasRole("ADMIN")
                        .requestMatchers("/api/ai-interactions/**").hasAnyRole("EMPLOYEE", "ADMIN", "CLIENT", "PARTNER")
                        .requestMatchers("/api/client-analytics/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/employee-analytics/**").hasRole("ADMIN")
                        .requestMatchers("/api/financial-analytics/**").hasRole("ADMIN")
                        .requestMatchers("/api/analytics/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
