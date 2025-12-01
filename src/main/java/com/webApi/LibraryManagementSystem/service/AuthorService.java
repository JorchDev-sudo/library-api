package com.webApi.LibraryManagementSystem.service;

import com.webApi.LibraryManagementSystem.model.AuthorModel;
import com.webApi.LibraryManagementSystem.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    //Post
    public AuthorModel postAuthor(AuthorModel author){
        return authorRepository.save(author);
    }

    //Put
    public AuthorModel putAuthor(Long id, AuthorModel authorDetails){
        AuthorModel author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found with id: " + id));

        author.setName(authorDetails.getName());
        return authorRepository.save(author);
    }

    //Get (byName)
    public Optional<AuthorModel> getAuthor(Long id){
        return authorRepository.findById(id);
    }

    //Delete (byName)
    public void deleteAuthor(Long id){
        authorRepository.deleteById(id);
    }
}
