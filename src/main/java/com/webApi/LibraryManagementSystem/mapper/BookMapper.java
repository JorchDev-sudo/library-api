package com.webApi.LibraryManagementSystem.mapper;

import com.webApi.LibraryManagementSystem.dto.AuthorResponseDTO;
import com.webApi.LibraryManagementSystem.dto.BookResponseDTO;
import com.webApi.LibraryManagementSystem.model.AuthorModel;
import com.webApi.LibraryManagementSystem.model.BookModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {
    private AuthorResponseDTO toAuthorResponseDTO(AuthorModel authorModel){
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(authorModel.getId());
        dto.setName(authorModel.getName());
        return dto;
    }

    public BookResponseDTO toBookResponseDTO(BookModel bookModel){
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(bookModel.getId());
        dto.setTitle(bookModel.getTitle());
        dto.setIsbn(bookModel.getIsbn());
        dto.setPublicationYear(bookModel.getPublicationYear());
        dto.setStock(bookModel.getStock());

        dto.setAuthors(bookModel.getAuthors().stream()
                .map(this::toAuthorResponseDTO)
                .collect(Collectors.toSet()));

        return dto;
    }
}
