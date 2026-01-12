package com.jorchdev.library_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.exceptions.UnauthorizedException;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import com.jorchdev.library_api.security.CustomUserDetails;
import com.jorchdev.library_api.services.JwtService;
import com.jorchdev.library_api.services.UserService;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import com.jorchdev.library_api.utils.DeleteAccountConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;

    @MockBean
    private CurrentUserProvider currentUserProvider;

    private CustomUserDetails mockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setPassword("password");
        user.setRole(Roles.READER);
        user.setName("user");

        return new CustomUserDetails(user);
    }

    @Test
    void shouldReturnCurrentUser() throws Exception {

        CustomUserDetails user = mockUser();

        UserResponse response = new UserResponse(1L, "user", user.getUsername(), null);

        when(currentUserProvider.getCurrentUser()).thenReturn(user);
        when(userService.findUser(1L)).thenReturn(response);

        mockMvc.perform(get("/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("user@mail.com"));
    }

    @Test
    void shouldFailWhenUserNotAuthenticated() throws Exception {

        when(currentUserProvider.getCurrentUser())
                .thenThrow(new UnauthorizedException("User not authenticated"));

        mockMvc.perform(get("/user/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUpdateCurrentUser() throws Exception {

        CustomUserDetails user = mockUser();

        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("New Name");

        UserResponse response = new UserResponse(1L, "New Name", user.getUsername(), null);

        when(currentUserProvider.getCurrentUser()).thenReturn(user);
        when(userService.updateUser(any(), eq(1L)))
                .thenReturn(response);

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void shouldDeleteCurrentUser_whenConfirmationPhraseMatches() throws Exception {

        CustomUserDetails user = mockUser();

        when(currentUserProvider.getCurrentUser()).thenReturn(user);

        String confirmation = DeleteAccountConstants.CONFIRMATION_PHRASE;

        mockMvc.perform(delete("/user/me/{confirmation}", confirmation))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(user.getId());
    }

    @Test
    void shouldFailDeleteWhenConfirmationPhraseDoesNotMatch() throws Exception {

        CustomUserDetails user = mockUser();

        when(currentUserProvider.getCurrentUser()).thenReturn(user);

        String invalidConfirmation =
                UriUtils.encodePathSegment(
                        "I am not sure",
                        StandardCharsets.UTF_8
                );

        mockMvc.perform(delete("/user/me/{confirmation}", invalidConfirmation))
                .andExpect(status().isBadRequest());

        verify(userService, never()).deleteUser(anyLong());
    }

    @Test
    void shouldFailDeleteWhenUserNotAuthenticated() throws Exception {

        when(currentUserProvider.getCurrentUser())
                .thenThrow(new UnauthorizedException("User not authenticated"));

        String confirmation = DeleteAccountConstants.CONFIRMATION_PHRASE;

        mockMvc.perform(delete("/user/me/{confirmation}", confirmation))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).deleteUser(anyLong());
    }


}
