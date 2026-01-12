package com.jorchdev.library_api.service;

import com.jorchdev.library_api.dto.request.create.BookRequest;
import com.jorchdev.library_api.dto.request.update.BookUpdateRequest;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.mappers.AuthorMapper;
import com.jorchdev.library_api.mappers.BookMapper;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.services.AuthorService;
import com.jorchdev.library_api.services.BookService;
import com.jorchdev.library_api.utils.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private UtilService utilService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldCreateBookAndAddToTheAuthor() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        BookRequest request = new BookRequest();
        request.setTitle("Metamorphosis");
        request.setAuthorsIds(List.of(1L));

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Metamorphosis");
        book.setAuthors(List.of(author));

        BookResponse response = new BookResponse(
                "Metamorphosis",
                List.of(AuthorMapper.toSummary(author)),
                null,
                null,
                10
        );

        when(utilService.authorHelper(1L)).thenReturn(author);
        when(bookMapper.toEntity(request, List.of(author))).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.createBook(request);

        assertThat(result).isEqualTo(response);

        verify(utilService).authorHelper(1L);
        verify(bookMapper).toEntity(request, List.of(author));
        verify(bookRepository).save(book);
        verify(authorService).addBook(1L, 1L);
    }

    @Test
    void shouldFindBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Metamorphosis");

        BookResponse response = new BookResponse(
                "Metamorphosis",
                null,
                null,
                null,
                10
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.findBook(1L);

        assertThat(result).isEqualTo(response);

        verify(bookRepository).findById(1L);
        verify(bookMapper).toResponse(book);
    }

    @Test
    void shouldFindAuthorBooks() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Metamorphosis");
        book.setAuthors(List.of(author));

        Page<Book> page = new PageImpl<>(List.of(book));

        BookResponse response = new BookResponse(
                "Metamorphosis",
                List.of(AuthorMapper.toSummary(author)),
                null,
                null,
                10
        );

        when(utilService.authorHelper(1L)).thenReturn(author);
        when(bookRepository.findByAuthors(
                eq(author),
                argThat(pageable ->
                        pageable.getPageNumber() == 0 &&
                                pageable.getPageSize() == 10 &&
                                Objects.requireNonNull(pageable.getSort()
                                                .getOrderFor("publicationDate"))
                                        .isDescending()
                )
        )).thenReturn(page);

        when(bookMapper.toResponse(book)).thenReturn(response);

        List<BookResponse> result =
                bookService.findBooks(1L, 0, 10, "desc");

        assertThat(result).containsExactly(response);

        verify(bookRepository).findByAuthors(eq(author), argThat(pageable -> true));
        verify(bookMapper).toResponse(book);
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book();
        book.setId(1L);

        BookUpdateRequest updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("Metamorfosis");

        BookResponse response = new BookResponse(
                "Metamorfosis",
                null,
                null,
                null,
                10
        );

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toResponse(book)).thenReturn(response);

        BookResponse result = bookService.updateBook(updateRequest, 1L);

        assertThat(result.title()).isEqualTo("Metamorfosis");

        verify(bookMapper).toUpdate(updateRequest, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toResponse(book);
    }

    @Test
    void shouldDeleteBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
    }
}
