package com.jorchdev.library_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorchdev.library_api.dto.request.LoginRequest;
import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.dto.response.LoginResponse;
import com.jorchdev.library_api.dto.response.RegisterResponse;
import com.jorchdev.library_api.services.AuthService;
import com.jorchdev.library_api.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest();
        request.setName("Jorch");
        request.setEmail("jorch@mail.com");
        request.setPassword("password123");

        RegisterResponse response =
                new RegisterResponse("User Created Successfully","jorch@mail.com", "Jorch");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("jorch@mail.com"))
                .andExpect(jsonPath("$.name").value("Jorch"));

    }

    @Test
    void shouldReturnBadRequest_whenRegisterFails() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Jorch");
        request.setEmail("jorge@example.com");
        request.setPassword("password123");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalStateException("Email already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenRegisterValidationFails() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("invalid-email");
        request.setPassword("123"); // password débil

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("jorge@example.com");
        request.setPassword("password123");

        LoginResponse response = new LoginResponse("jwt-token", request.getEmail(), "Jorch");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.name").value("Jorch"));
    }

    @Test
    void shouldReturnUnauthorized_whenLoginFails() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("jorge@example.com");
        request.setPassword("wrong-password");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnBadRequest_whenLoginValidationFails() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid-email");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOk_forTestEndpoint() throws Exception {
        mockMvc.perform(get("/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Auth working correctly"));
    }
}
