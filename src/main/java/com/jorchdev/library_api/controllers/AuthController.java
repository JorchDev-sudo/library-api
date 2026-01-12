package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.dto.request.LoginRequest;
import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.dto.response.LoginResponse;
import com.jorchdev.library_api.dto.response.RegisterResponse;
import com.jorchdev.library_api.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Operation(summary = "Endpoint público para el registro de usuarios")
    @ApiResponse(responseCode = "201", description = "Envía un RegisterResponse")
    @ApiResponse(responseCode = "400", description = "Email already exists")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @RequestBody @Valid RegisterRequest request) {

        try {
            RegisterResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Endpoint público para el login de usuarios")
    @ApiResponse(responseCode = "200", description = "Envía un LoginResponse")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request)
    {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Endpoint público para el test de autenticación")
    @ApiResponse(responseCode = "200", description = "Auth working correctly")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth working correctly");
    }
}