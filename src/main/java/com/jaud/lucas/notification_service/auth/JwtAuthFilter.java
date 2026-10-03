package com.jaud.lucas.notification_service.auth;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    
    @Override
    protected void doFilterInternal(@NonNull  HttpServletRequest request, @NonNull  HttpServletResponse response, @NonNull  FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization").trim();
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try{
            final String username = jwtService.extractUsername(jwt);

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                 UserDetails userDetails = userService.loadUserByUsername(username);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token");
            return;
        }


    }
    
}
