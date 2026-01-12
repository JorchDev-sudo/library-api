package com.jorchdev.library_api.security;

import com.jorchdev.library_api.security.JwtAuthenticationFilter;
import com.jorchdev.library_api.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtService jwtService;

    @Mock
    UserDetailsService userDetailsService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotAuthenticate_whenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldAuthenticateUser_whenTokenIsValid() throws Exception {
        String token = "valid-jwt-token";
        String email = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn(email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("password")
                .roles("READER")
                .build();

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isEqualTo(userDetails);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticate_whenTokenIsInvalid() throws Exception {
        String token = "invalid-jwt-token";
        String email = "user@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenReturn(email);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("password")
                .roles("READER")
                .build();

        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldContinueFilterChain_whenJwtExtractionFails() throws Exception {
        String token = "broken-token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractEmail(token)).thenThrow(new RuntimeException("JWT error"));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }
}
