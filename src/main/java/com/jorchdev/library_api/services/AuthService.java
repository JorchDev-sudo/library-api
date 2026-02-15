package com.jorchdev.library_api.services;

import com.jorchdev.library_api.dto.request.LoginRequest;
import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.dto.response.LoginResponse;
import com.jorchdev.library_api.dto.response.RegisterResponse;
import com.jorchdev.library_api.mappers.AuthMapper;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            AuthMapper authMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtservice
    ) {
        this.userRepository = userRepository;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtservice;
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = authMapper.toEntity(request);

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
        return new RegisterResponse("User created successfully", user.getEmail(), user.getName());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, user.getEmail(), user.getName());
    }
}