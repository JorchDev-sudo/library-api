package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.config.SecurityConfig;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.security.CustomUserDetails;
import com.jorchdev.library_api.services.CustomUserDetailsService;
import com.jorchdev.library_api.services.JwtService;
import com.jorchdev.library_api.services.LoanService;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
@Import(SecurityConfig.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @MockBean
    private CurrentUserProvider currentUserProvider;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    private void mockAuthenticatedUser() {
        User user = new User();
        user.setEmail("example@mail.com");
        user.setId(1L);

        when(currentUserProvider.getCurrentUser()).thenReturn(new CustomUserDetails(user));
    }


    @Test
    @WithMockUser
    void shouldCreateLoanSuccessfully() throws Exception {
        mockAuthenticatedUser();

        LoanResponse response = new LoanResponse(
                1L,
                "example",
                "example@mail.com",
                null,
                null,
                null);

        when(loanService.createLoan(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "bookId": 1
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenBookDoesNotExist() throws Exception {
        mockAuthenticatedUser();

        when(loanService.createLoan(any(), eq(1L)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "bookId": 99
                }
            """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnConflictWhenBookAlreadyLoaned() throws Exception {
        mockAuthenticatedUser();

        when(loanService.createLoan(any(), eq(1L)))
                .thenThrow(new IllegalStateException());

        mockMvc.perform(post("/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "bookId": 1
                }
            """))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void shouldReturnBookSuccessfully() throws Exception {
        mockMvc.perform(put("/loan/1/return"))
                .andExpect(status().isOk());

        verify(loanService).returnBook(1L);
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenLoanDoesNotExist() throws Exception {
        doThrow(new IllegalArgumentException())
                .when(loanService).returnBook(1L);

        mockMvc.perform(put("/loan/1/return"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnConflictWhenLoanAlreadyReturned() throws Exception {
        doThrow(new IllegalStateException())
                .when(loanService).returnBook(1L);

        mockMvc.perform(put("/loan/1/return"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void shouldGetLoansForCurrentUser() throws Exception {
        mockAuthenticatedUser();

        when(loanService.findLoans(eq(1L), eq(0), eq(10), eq("desc")))
                .thenReturn(List.of(
                        new LoanResponse(
                        1L,
                        "example",
                        "example@mail.com",
                        null,
                        null,
                        null)));

        mockMvc.perform(get("/loan"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

}
