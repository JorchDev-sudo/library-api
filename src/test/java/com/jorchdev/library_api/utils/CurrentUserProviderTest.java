package com.jorchdev.library_api.utils;

import com.jorchdev.library_api.exceptions.UnauthorizedException;
import com.jorchdev.library_api.security.CustomUserDetails;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CurrentUserProviderTest {

    private final CurrentUserProvider currentUserProvider = new CurrentUserProvider();

    @Mock
    CustomUserDetails customUserDetails;

    @AfterEach
    void cleanUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getCurrentUser_shouldReturnUser_whenAuthenticated() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails,
                        null,
                        customUserDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails result = currentUserProvider.getCurrentUser();

        assertThat(result).isEqualTo(customUserDetails);
    }

    @Test
    void getCurrentUser_shouldThrowException_whenAuthenticationIsNull() {
        SecurityContextHolder.clearContext();

        assertThatThrownBy(currentUserProvider::getCurrentUser)
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("User not authenticated");
    }

    @Test
    void getCurrentUser_shouldThrowException_whenNotAuthenticated() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(null, null);
        authentication.setAuthenticated(false);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThatThrownBy(currentUserProvider::getCurrentUser)
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("User not authenticated");
    }

    @Test
    void getCurrentUser_shouldThrowException_whenPrincipalIsInvalid() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid-principal",
                        null
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertThatThrownBy(currentUserProvider::getCurrentUser)
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("User not authenticated");
    }
}
