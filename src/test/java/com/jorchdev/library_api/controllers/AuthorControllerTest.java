package com.jorchdev.library_api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jorchdev.library_api.config.SecurityConfig;
import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.security.JwtAuthenticationFilter;
import com.jorchdev.library_api.services.AuthorService;
import com.jorchdev.library_api.services.CustomUserDetailsService;
import com.jorchdev.library_api.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@Import(SecurityConfig.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldCreateAuthorSuccessfully() throws Exception {
        AuthorRequest request = new AuthorRequest();
        request.setName("Example");

        AuthorResponse response = new AuthorResponse(1L, "Example", null);

        when(authorService.createAuthor(any())).thenReturn(response);

        mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Example"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidCreateAuthorForNonLibrarian() throws Exception {
        AuthorRequest request = new AuthorRequest();
        request.setName("Example");

        mockMvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldGetAuthorById() throws Exception {
        AuthorResponse response = new AuthorResponse(1L, "Example", null);
        when(authorService.findAuthor(1L)).thenReturn(response);

        mockMvc.perform(get("/author/1"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Example"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldUpdateAuthorSuccessfully() throws Exception {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("ExampleUpdate");

        AuthorResponse response = new AuthorResponse(1L, "ExampleUpdate", null);

        when(authorService.updateAuthor(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ExampleUpdate"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidUpdateAuthorForNonLibrarian() throws Exception {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("ExampleUpdate");

        mockMvc.perform(put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldDeleteAuthorSuccessfully() throws Exception {
        mockMvc.perform(delete("/author/1"))
                .andExpect(status().isNoContent());

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidDeleteAuthorForNonLibrarian() throws Exception {
        mockMvc.perform(delete("/author/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/author/1"))
                .andExpect(status().isUnauthorized());
    }
}
