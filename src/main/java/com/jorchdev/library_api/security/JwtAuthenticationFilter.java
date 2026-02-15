package com.jorchdev.library_api.security;

import com.jorchdev.library_api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        log.info("Auth header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("No JWT found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        log.info("JWT extracted");

        String email;
        try {
            email = jwtService.extractEmail(jwt);
            log.info("Email from JWT: {}", email);
        } catch (Exception e) {
            log.error("Error extracting email from JWT", e);
            filterChain.doFilter(request, response);
            return;
        }

        Authentication existingAuth =
                SecurityContextHolder.getContext().getAuthentication();

        if (email != null && existingAuth == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            log.info("User loaded: {}", userDetails.getUsername());
            log.info("Authorities: {}", userDetails.getAuthorities());

            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("JWT is valid");

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authToken);

                log.info("Authentication set in SecurityContext");
            } else {
                log.warn("JWT is NOT valid");
            }
        }

        filterChain.doFilter(request, response);
    }

}