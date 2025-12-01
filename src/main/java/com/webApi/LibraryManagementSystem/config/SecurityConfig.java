package com.webApi.LibraryManagementSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails librarian = User.builder()
                .username("librarian")
                .password(encoder.encode("password123"))
                .roles("LIBRARIAN")
                .build();

        UserDetails reader = User.builder()
                .username("reader")
                .password(encoder.encode("readerpass"))
                .roles("READER")
                .build();

        return new InMemoryUserDetailsManager(librarian, reader);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/books").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/books/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/books/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/author/post").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE,"/author/delete/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.POST,"/loans").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT,"/loans/**").hasRole("LIBRARIAN")

                        .requestMatchers(HttpMethod.GET, "/books").hasAnyRole("LIBRARIAN", "READER")
                        .requestMatchers(HttpMethod.GET, "/author/get/**").hasAnyRole("LIBRARIAN", "READER")

                        .requestMatchers("/h2-console/**").hasRole("LIBRARIAN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        http.headers(headers->headers.frameOptions(frameOptions-> frameOptions.sameOrigin()));

        return http.build();
    }

}
