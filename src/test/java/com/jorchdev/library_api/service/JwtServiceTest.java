package com.jorchdev.library_api.service;

import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
    }

    @Test
    void shouldGenerateToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setName("Jorch");

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void shouldExtractEmailFromToken() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setName("Jorch");

        String token = jwtService.generateToken(user);

        String email = jwtService.extractEmail(token);

        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    void shouldExtractUserIdFromToken() {
        User user = new User();
        user.setId(42L);
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setName("Jorch");

        String token = jwtService.generateToken(user);

        Long userId = jwtService.extractUserId(token);

        assertThat(userId).isEqualTo(42L);
    }

    @Test
    void shouldValidateToken() {
        User user = new User();
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setName("Jorch");

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("jorgecoteralopez@gmail.com")
                .password("password")
                .roles("READER")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenTokenIsExpired() throws ExpiredJwtException{
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1L);

        User user = new User();
        user.setEmail("jorgecoteralopez@gmail.com");

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("password")
                .roles("READER")
                .build();

        assertThatThrownBy(() -> jwtService.isTokenValid(token, userDetails))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void isTokenValid_shouldReturnFalse_whenEmailDoesNotMatch() {
        User user = new User();
        user.setEmail("jorge@mail.com");

        String token = jwtService.generateToken(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("other@mail.com")
                .password("password")
                .roles("READER")
                .build();

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    void extractEmail_shouldThrowException_whenTokenIsInvalid() {
        String invalidToken = "invalid.token.value";

        assertThatThrownBy(() -> jwtService.extractEmail(invalidToken))
                .isInstanceOf(Exception.class);
    }
}