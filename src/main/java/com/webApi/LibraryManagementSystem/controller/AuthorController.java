package com.webApi.LibraryManagementSystem.controller;

import com.webApi.LibraryManagementSystem.model.AuthorModel;
import com.webApi.LibraryManagementSystem.service.AuthorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/author")
@SecurityRequirement(name = "basicAuth")
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

    @GetMapping("/get/{id}")
    public ResponseEntity<AuthorModel> getAuthor(@PathVariable Long id){
        return authorService.getAuthor(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/put/{id}")
    public ResponseEntity<AuthorModel> putAuthor(@PathVariable Long id, @RequestBody AuthorModel authorDetails){
        try{
            AuthorModel updatedAuthor = authorService.putAuthor(id, authorDetails);
            return ResponseEntity.ok(updatedAuthor);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

}
