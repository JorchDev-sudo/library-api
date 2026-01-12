package com.jorchdev.library_api.services;

import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.UserRepository;
import com.jorchdev.library_api.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private static final Logger log =
            LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Loading user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        log.info("User found. Role: {}", user.getRole());

        return new CustomUserDetails(user);
    }

}