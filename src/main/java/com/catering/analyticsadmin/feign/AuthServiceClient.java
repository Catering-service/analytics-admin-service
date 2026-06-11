package com.catering.analyticsadmin.feign;

import com.catering.analyticsadmin.model.dto.external.AuthUserCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @PostMapping("/auth/internal/users")
    Map<String, Object> createUser(@RequestBody AuthUserCreateRequest request);
}
