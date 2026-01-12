package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.BookRequest;
import com.jorchdev.library_api.dto.request.update.BookUpdateRequest;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.dto.summary.BookSummary;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class BookMapper {
    public Book toEntity (BookRequest bookRequest, List<Author> authors) {
        Book newBook = new Book();
        newBook.setTitle(bookRequest.getTitle());
        newBook.setIsbn(bookRequest.getIsbn());
        newBook.setPublicationDate(bookRequest.getPublicationDate());
        newBook.setStock(bookRequest.getStock());
        newBook.setAuthors(authors);

        return newBook;
    }

    public BookResponse toResponse (Book book){
        return new BookResponse (
                book.getTitle(),
                book.getAuthors()
                        .stream()
                        .map(AuthorMapper::toSummary).toList(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getStock());
    }

    public abstract void toUpdate (BookUpdateRequest updateRequest, @MappingTarget Book book);

    public static BookSummary toSummary(Book book) {
        return new BookSummary(book.getId(), book.getTitle());
    }
}