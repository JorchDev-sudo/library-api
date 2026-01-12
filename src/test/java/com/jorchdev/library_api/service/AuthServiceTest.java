package com.jorchdev.library_api.service;

import com.jorchdev.library_api.dto.request.LoginRequest;
import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.dto.response.LoginResponse;
import com.jorchdev.library_api.dto.response.RegisterResponse;
import com.jorchdev.library_api.mappers.AuthMapper;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.UserRepository;
import com.jorchdev.library_api.services.AuthService;
import com.jorchdev.library_api.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegister() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Jorch");
        request.setEmail("jorgecoteralopez@gmail.com");
        request.setPassword("password123");

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(authMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn("hashedPassword");

        RegisterResponse response = authService.register(request);

        assertThat(response.email()).isEqualTo(request.getEmail());
        assertThat(response.name()).isEqualTo(request.getName());
        assertThat(user.getPassword()).isEqualTo("hashedPassword");

        verify(userRepository).existsByEmail(request.getEmail());
        verify(authMapper).toEntity(request);
        verify(passwordEncoder).encode(request.getPassword());
        verify(userRepository).save(user);
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void shouldLogin() {
        User user = new User();
        user.setId(1L);
        user.setName("Jorch");
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setPassword("hashedPassword");

        LoginRequest request = new LoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        LoginResponse response = authService.login(request);

        assertThat(response.email()).isEqualTo(user.getEmail());
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.token()).isEqualTo("jwt-token");

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(jwtService).generateToken(user);
        verifyNoMoreInteractions(userRepository, authMapper);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Jorch");
        request.setEmail("jorgecoteralopez@gmail.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userRepository).existsByEmail(request.getEmail());
        verifyNoInteractions(authMapper, passwordEncoder, jwtService);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnLogin() {
        LoginRequest request = new LoginRequest();
        request.setEmail("notfound@gmail.com");
        request.setPassword("password123");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(org.springframework.security.authentication.BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        verify(userRepository).findByEmail(request.getEmail());
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        User user = new User();
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setPassword("hashedPassword");

        LoginRequest request = new LoginRequest();
        request.setEmail(user.getEmail());
        request.setPassword("wrongPassword");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(org.springframework.security.authentication.BadCredentialsException.class)
                .hasMessage("Invalid credentials");

        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verifyNoInteractions(jwtService);
    }



}
