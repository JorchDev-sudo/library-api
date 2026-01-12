package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.config.SecurityConfig;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.services.BookService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldCreateBookSuccessfully() throws Exception {
        BookResponse response = new BookResponse(
                "Example",
                List.of(),
                "1234-5678",
                null,
                10);

        when(bookService.createBook(any())).thenReturn(response);

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "title": "Example",
                  "stock": 10,
                  "authorIds": [1]
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Example"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldReturnBadRequestWhenBookIsInvalid() throws Exception {
        when(bookService.createBook(any()))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidCreateBookForNonLibrarian() throws Exception {
        mockMvc.perform(post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "title": "Test",
                  "stock": 5,
                  "authorIds": [1]
                }
            """))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldFindBookById() throws Exception {
        BookResponse response = new BookResponse(
                "Example",
                List.of(),
                "1234-5678",
                null,
                10);

        when(bookService.findBook(1L)).thenReturn(response);

        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Example"));
    }

    @Test
    @WithMockUser
    void shouldGetBooksByAuthor() throws Exception {
        BookResponse response = new BookResponse(
                "Example",
                List.of(),
                "1234-5678",
                null,
                10);

        when(bookService.findBooks(eq(1L), eq(0), eq(10), eq("desc")))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/book/author/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Example"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldUpdateBookSuccessfully() throws Exception {
        BookResponse response = new BookResponse(
                "Updated",
                List.of(),
                "1234-5678",
                null,
                15);
        when(bookService.updateBook(any(), eq(1L))).thenReturn(response);

        mockMvc.perform(put("/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "title": "Updated",
                  "stock": 15
                }
            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidUpdateBookForNonLibrarian() throws Exception {
        mockMvc.perform(put("/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void shouldDeleteBookSuccessfully() throws Exception {
        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    @WithMockUser(roles = "READER")
    void shouldForbidDeleteBookForNonLibrarian() throws Exception {
        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isForbidden());
    }

}
