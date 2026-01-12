package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.dto.summary.AuthorSummary;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.utils.UtilService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class AuthorMapper {

    public Author toEntity (AuthorRequest request, @Context UtilService utilService){
        Author newAuthor = new Author();
        newAuthor.setName(request.getName());

        if (request.getBooksId() == null){
            return newAuthor;
        }

        for (long bookId : request.getBooksId()){
            newAuthor.addBook(utilService.bookHelper(bookId));
        }

        return newAuthor;
    }

    public AuthorResponse toResponse (Author author){
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getBooks()
                        .stream()
                        .map(BookMapper::toSummary).toList());
    }

    public static AuthorSummary toSummary (Author author){
        return new AuthorSummary(author.getId(), author.getName());
    }

    public abstract void toUpdate (
            AuthorUpdateRequest updateRequest,
            @MappingTarget Author author);
}
