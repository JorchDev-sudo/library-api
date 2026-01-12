package com.jorchdev.library_api.service;

import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import com.jorchdev.library_api.repositories.UserRepository;
import com.jorchdev.library_api.security.CustomUserDetails;
import com.jorchdev.library_api.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void shouldLoadUserByEmail() {
        String email = "user@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        user.setPassword("$2a$10$hashedPassword...");
        user.setName("user");
        user.setRole(Roles.READER);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("$2a$10$hashedPassword...");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_READER");
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);


        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldLoadLibrarian() {
        String email = "admin@example.com";

        User admin = new User();
        admin.setId(2L);
        admin.setEmail(email);
        admin.setPassword("$2a$10$adminPassword...");
        admin.setName("Admin User");
        admin.setRole(Roles.LIBRARIAN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(admin));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_LIBRARIAN");
        assertThat(userDetails).isInstanceOf(CustomUserDetails.class);

    }
}