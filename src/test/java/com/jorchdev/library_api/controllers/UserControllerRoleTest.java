package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.MethodSecurityTestConfig;
import com.jorchdev.library_api.config.SecurityConfig;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.services.CustomUserDetailsService;
import com.jorchdev.library_api.services.JwtService;
import com.jorchdev.library_api.services.UserService;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerRoleTest {
    @MockBean
    UserService userService;

    @MockBean
    JwtService jwtService;

    @MockBean
    CustomUserDetailsService customUserDetailsService;

    @MockBean
    CurrentUserProvider currentUserProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldAllowLibrarianToGetUserById() throws Exception {

        UserResponse response = new UserResponse(
                1L,
                "Jorch",
                "jorch@email.com",
                null
        );

        when(userService.findUser(1L)).thenReturn(response);

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("jorch@email.com"));

        verify(userService).findUser(1L);
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidGetUserByIdForNonLibrarian() throws Exception {
        mockMvc.perform(get("/user/{id}", 2L))
                .andExpect(status().isForbidden());

        verify(userService, never()).findUser(anyLong());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/user/{id}", 1L))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).findUser(anyLong());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldAllowLibrarianToDeleteUser() throws Exception {

        mockMvc.perform(delete("/user/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldForbidDeleteUserForNonLibrarian() throws Exception {

        mockMvc.perform(delete("/user/{id}", 1L))
                .andExpect(status().isForbidden());

        verify(userService, never()).deleteUser(anyLong());
    }
}
