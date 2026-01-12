package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.dto.request.create.BookRequest;
import com.jorchdev.library_api.dto.request.update.BookUpdateRequest;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(
            BookService bookService)
    {
        this.bookService = bookService;
    }

    @Operation(summary = "Endpoint con @PreAuthorize para la creación de Books")
    @ApiResponse(responseCode = "201", description = "Envía un BookResponse")
    @ApiResponse(responseCode = "400")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody BookRequest bookDetails){
        try{
            BookResponse savedBook = bookService.createBook(bookDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Endpoint para un get de un Book")
    @ApiResponse(responseCode = "200", description = "Envía un BookResponse")
    @ApiResponse(responseCode = "401")
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findBook(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findBook(id));

    }

    @Operation(summary = "Endpoint para un get de los Books de un Author")
    @ApiResponse(responseCode = "200", description = "Envía una lista paginada de BookResponses")
    @ApiResponse(responseCode = "401")
    @GetMapping("author/{authorId}")
    public ResponseEntity<List<BookResponse>> getBooks(
            @PathVariable long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction)
    {
        return ResponseEntity.ok(bookService.findBooks(authorId, page, size, direction));
    }

    @Operation(summary = "Endpoint con @PreAuthorize para el update de Books")
    @ApiResponse(responseCode = "200", description = "Envía un BookResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> putBook (@PathVariable Long id, @RequestBody BookUpdateRequest updateRequest){
        return ResponseEntity.ok(bookService.updateBook(updateRequest, id));
    }

    @Operation(summary = "Endpoint con @PreAuthorize para el delete de Books")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
