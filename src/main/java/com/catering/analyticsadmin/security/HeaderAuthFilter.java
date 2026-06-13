package com.catering.analyticsadmin.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class HeaderAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String rolesHeader = request.getHeader("X-Roles");
        String user = request.getHeader("X-User");

        if (rolesHeader != null) {

            List<SimpleGrantedAuthority> authorities =
                    Arrays.stream(rolesHeader.replace("[", "")
                                    .replace("]", "")
                                    .split(","))
                            .map(String::trim)
                            .map(role -> new SimpleGrantedAuthority(role))
                            .toList();

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    authorities
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
