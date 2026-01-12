package com.jorchdev.library_api.services;

import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.mappers.AuthorMapper;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.repositories.AuthorRepository;
import com.jorchdev.library_api.utils.UtilService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final UtilService utilService;

    public AuthorService(
            AuthorRepository authorRepository,
            AuthorMapper authorMapper,
            UtilService utilService){
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.utilService = utilService;
    }

    public AuthorResponse createAuthor (AuthorRequest authorRequest) {
        Author newAuthor = authorMapper.toEntity(authorRequest, utilService);
        Author savedAuthor = authorRepository.save(newAuthor);

        return authorMapper.toResponse(savedAuthor);
    }

    public AuthorResponse findAuthor (long id){
        Author author = authorRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return authorMapper.toResponse(author);
    }

    public void addBook (long authorId , long bookId){
        Author author = authorRepository.findById(authorId)
                            .orElseThrow(EntityNotFoundException::new);
        author.addBook(utilService.bookHelper(bookId));

        authorRepository.save(author);
    }

    public AuthorResponse updateAuthor (AuthorUpdateRequest updateRequest, long id){
        Author author = authorRepository.findById(id)
                            .orElseThrow(EntityNotFoundException::new);

        authorMapper.toUpdate(updateRequest, author);

        authorRepository.save(author);
        return authorMapper.toResponse(author);
    }

    public void deleteAuthor(Long id){
        authorRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new);

        authorRepository.deleteById(id);
    }
}
