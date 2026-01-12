package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.BookRequest;
import com.jorchdev.library_api.dto.request.update.BookUpdateRequest;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.dto.summary.BookSummary;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {

    private final BookMapper bookMapper = new BookMapperImpl();

    @Test
    void toEntity_shouldCreateBookFromRequestAndAuthors() {
        BookRequest request = new BookRequest();
        request.setTitle("Metamorphosis");
        request.setIsbn("123-456");
        request.setPublicationDate(LocalDate.of(1915, 10, 1).atStartOfDay());
        request.setStock(10L);

        Author author = new Author();
        author.setId(1L);
        author.setName("Franz Kafka");

        List<Author> authors = List.of(author);

        Book book = bookMapper.toEntity(request, authors);

        assertThat(book.getTitle()).isEqualTo("Metamorphosis");
        assertThat(book.getIsbn()).isEqualTo("123-456");
        assertThat(book.getPublicationDate()).isEqualTo(LocalDate.of(1915, 10, 1).atStartOfDay());
        assertThat(book.getStock()).isEqualTo(10);
        assertThat(book.getAuthors()).containsExactly(author);
    }

    @Test
    void toResponse_shouldMapBookToBookResponse() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Franz Kafka");

        Book book = new Book();
        book.setTitle("Metamorphosis");
        book.setIsbn("123-456");
        book.setPublicationDate(LocalDate.of(1915, 10, 1).atStartOfDay());
        book.setStock(5L);
        book.setAuthors(List.of(author));

        BookResponse response = bookMapper.toResponse(book);

        assertThat(response.title()).isEqualTo("Metamorphosis");
        assertThat(response.isbn()).isEqualTo("123-456");
        assertThat(response.publicationDate()).isEqualTo(LocalDate.of(1915, 10, 1).atStartOfDay());
        assertThat(response.stock()).isEqualTo(5);
        assertThat(response.authors())
                .containsExactly(AuthorMapper.toSummary(author));
    }

    @Test
    void toUpdate_shouldUpdateOnlyNonNullFields() {
        Author author = new Author();
        author.setId(1L);

        Book book = new Book();
        book.setId(10L);
        book.setTitle("Old Title");
        book.setStock(3L);
        book.setAuthors(List.of(author));

        BookUpdateRequest updateRequest = new BookUpdateRequest();
        updateRequest.setTitle("New Title");
        updateRequest.setStock(20L);

        bookMapper.toUpdate(updateRequest, book);

        assertThat(book.getId()).isEqualTo(10L);
        assertThat(book.getTitle()).isEqualTo("New Title");
        assertThat(book.getStock()).isEqualTo(20);
        assertThat(book.getAuthors()).containsExactly(author);
    }

    @Test
    void toUpdate_shouldNotModifyEntityWhenUpdateRequestIsEmpty() {
        Book book = new Book();
        book.setTitle("Metamorphosis");
        book.setStock(10L);

        BookUpdateRequest updateRequest = new BookUpdateRequest();

        bookMapper.toUpdate(updateRequest, book);

        assertThat(book.getTitle()).isEqualTo("Metamorphosis");
        assertThat(book.getStock()).isEqualTo(10);
    }

    @Test
    void toSummary_shouldCreateBookSummary() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Metamorphosis");

        BookSummary summary = BookMapper.toSummary(book);

        assertThat(summary.id()).isEqualTo(1L);
        assertThat(summary.title()).isEqualTo("Metamorphosis");
    }
}
