package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.mappers.AuthorMapper;
import com.jorchdev.library_api.mappers.AuthorMapperImpl;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.utils.UtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorMapperTest {

    @Mock
    UtilService utilService;

    private final AuthorMapper authorMapper = new AuthorMapperImpl();

    @Test
    void shouldCreateEntity() {
        AuthorRequest request = new AuthorRequest();
        request.setName("Kafka");
        request.setBooksId(List.of(1L));

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Metamorphosis");

        when(utilService.bookHelper(1L)).thenReturn(book);

        Author author = authorMapper.toEntity(request, utilService);

        assertThat(author.getName()).isEqualTo("Kafka");
        assertThat(author.getBooks()).containsExactly(book);

        verify(utilService).bookHelper(1L);
    }

    @Test
    void shouldCreateResponse() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        AuthorResponse response = authorMapper.toResponse(author);

        assertThat(response.name()).isEqualTo("Kafka");
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.bookSummaries()).isEmpty();
    }

    @Test
    void shouldUpdateEntity() {
        AuthorUpdateRequest updateRequest = new AuthorUpdateRequest();
        updateRequest.setName("Franz Kafka");

        Author author = new Author();
        author.setId(1L);
        author.setName("Kafka");

        authorMapper.toUpdate(updateRequest, author);

        assertThat(author.getName()).isEqualTo("Franz Kafka");
        assertThat(author.getId()).isEqualTo(1L);
    }
}
