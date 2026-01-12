package com.jorchdev.library_api.service;

import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.mappers.AuthorMapper;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.repositories.AuthorRepository;
import com.jorchdev.library_api.services.AuthorService;
import com.jorchdev.library_api.utils.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private UtilService utilService;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void shouldCreateAuthor() {
        AuthorRequest request = new AuthorRequest();
        request.setName("Kafka");

        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        AuthorResponse response =
                new AuthorResponse(1L, "Kafka", null);

        when(authorMapper.toEntity(request, utilService)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);
        when(authorMapper.toResponse(author)).thenReturn(response);

        AuthorResponse result = authorService.createAuthor(request);

        assertThat(result).isEqualTo(response);

        verify(authorMapper).toEntity(request, utilService);
        verify(authorRepository).save(author);
        verify(authorMapper).toResponse(author);
    }

    @Test
    void shouldFindAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        when(authorRepository.findById(1L))
                .thenReturn(Optional.of(author));
        when(authorMapper.toResponse(author))
                .thenReturn(new AuthorResponse(1L, "Kafka", null));

        AuthorResponse response = authorService.findAuthor(1L);

        assertThat(response.name()).isEqualTo("Kafka");

        verify(authorRepository).findById(1L);
        verify(authorMapper).toResponse(author);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        when(authorRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.findAuthor(99L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        verify(authorRepository).findById(99L);
        verifyNoInteractions(authorMapper);
    }

    @Test
    void shouldAddBookToAuthor() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book();
        book.setId(2L);

        when(authorRepository.findById(1L))
                .thenReturn(Optional.of(author));
        when(utilService.bookHelper(2L))
                .thenReturn(book);

        authorService.addBook(1L, 2L);

        assertThat(author.getBooks()).contains(book);

        verify(authorRepository).findById(1L);
        verify(utilService).bookHelper(2L);
        verify(authorRepository).save(author);
    }

    @Test
    void shouldThrowExceptionWhenAddingBookToNonExistingAuthor() {
        when(authorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.addBook(1L, 2L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        verify(authorRepository).findById(1L);
        verifyNoInteractions(utilService);
    }

    @Test
    void shouldUpdateAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("Franz Kafka");

        when(authorRepository.findById(1L))
                .thenReturn(Optional.of(author));
        when(authorRepository.save(author))
                .thenReturn(author);
        when(authorMapper.toResponse(author))
                .thenReturn(new AuthorResponse(1L, "Franz Kafka", null));

        AuthorResponse response =
                authorService.updateAuthor(updateRequest, 1L);

        assertThat(response.name()).isEqualTo("Franz Kafka");

        verify(authorRepository).findById(1L);
        verify(authorMapper).toUpdate(updateRequest, author);
        verify(authorRepository).save(author);
        verify(authorMapper).toResponse(author);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingAuthor() {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("Test");

        when(authorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.updateAuthor(updateRequest, 1L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        verify(authorRepository).findById(1L);
        verifyNoInteractions(authorMapper);
    }

    @Test
    void shouldDeleteAuthor() {
        Author author = new Author();
        author.setId(1L);

        when(authorRepository.findById(1L))
                .thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        verify(authorRepository).findById(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingAuthor() {
        when(authorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.deleteAuthor(1L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);

        verify(authorRepository).findById(1L);
        verify(authorRepository, never()).deleteById(anyLong());
    }
}
