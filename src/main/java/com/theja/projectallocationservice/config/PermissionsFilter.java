package com.theja.projectallocationservice.config;

import com.theja.projectallocationservice.models.PermissionName;
import com.theja.projectallocationservice.models.RequestContext;
import com.theja.projectallocationservice.services.UserServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class PermissionsFilter extends OncePerRequestFilter {
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private RequestContext requestContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            throw new RuntimeException("Missing authorization header");
        }
        java.util.List<PermissionName> permissions = userServiceClient.getPermissions(authHeader);
        requestContext.setPermissions(permissions);
        requestContext.setLoggedinUser(userServiceClient.getUser(authHeader));
        filterChain.doFilter(request, response);
    }
}

