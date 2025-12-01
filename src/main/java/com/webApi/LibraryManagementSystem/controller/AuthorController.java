package com.webApi.LibraryManagementSystem.controller;

import com.webApi.LibraryManagementSystem.model.AuthorModel;
import com.webApi.LibraryManagementSystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @PostMapping("/post")
    public ResponseEntity<AuthorModel> postAuthor(@RequestBody AuthorModel author){
        AuthorModel newAuthor = authorService.postAuthor(author);
        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<AuthorModel> getAuthor(@PathVariable Long id){
        return authorService.getAuthor(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<AuthorModel> putAuthor(@PathVariable Long id, @RequestBody AuthorModel authorDetails){
        try{
            AuthorModel updatedAuthor = authorService.putAuthor(id, authorDetails);
            return ResponseEntity.ok(updatedAuthor);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
