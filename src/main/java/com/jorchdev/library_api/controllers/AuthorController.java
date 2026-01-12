package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.dto.request.create.AuthorRequest;
import com.jorchdev.library_api.dto.request.update.AuthorUpdateRequest;
import com.jorchdev.library_api.dto.response.AuthorResponse;
import com.jorchdev.library_api.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @Operation(summary = "Endpoint con @PreAuthorize para la creación de Author")
    @ApiResponse(responseCode = "201", description = "Envía un AuthorResponse")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<AuthorResponse> postAuthor(@RequestBody AuthorRequest authorDetails){
        AuthorResponse newAuthor = authorService.createAuthor(authorDetails);

        return new ResponseEntity<>(newAuthor, HttpStatus.CREATED);
    }

    @Operation(summary = "Endpoint para el get de Author")
    @ApiResponse(responseCode = "202", description = "Envía un AuthorResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable Long id){
        return new ResponseEntity<>(authorService.findAuthor(id), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Endpoint con @PreAuthorize para el delete de Author")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id){
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Endpoint con @PreAuthorize para el update de Author")
    @ApiResponse(responseCode = "200", description = "Envía un AuthorResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> putAuthor (@PathVariable Long id, @RequestBody AuthorUpdateRequest updateRequest){
        return ResponseEntity.ok(authorService.updateAuthor(updateRequest, id));
    }


}
