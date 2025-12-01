package com.webApi.LibraryManagementSystem.service;

import com.webApi.LibraryManagementSystem.mapper.BookMapper;
import com.webApi.LibraryManagementSystem.dto.BookRequestDTO;
import com.webApi.LibraryManagementSystem.dto.BookResponseDTO;
import com.webApi.LibraryManagementSystem.model.AuthorModel;
import com.webApi.LibraryManagementSystem.model.BookModel;
import com.webApi.LibraryManagementSystem.repository.AuthorRepository;
import com.webApi.LibraryManagementSystem.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper){
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional
    public BookResponseDTO saveBook(BookRequestDTO requestDTO) {
        Set<AuthorModel> authors = requestDTO.getAuthorsIds().stream()
                .map(authorId -> authorRepository.findById(authorId)
                        .orElseThrow(() -> new IllegalArgumentException("Author with id: " + authorId + " not found")))
                .collect(Collectors.toSet());

        BookModel book = new BookModel();
        book.setTitle(requestDTO.getTitle());
        book.setIsbn(requestDTO.getIsbn());
        book.setPublicationYear(requestDTO.getPublicationYear());
        book.setStock(requestDTO.getStock());

        for (AuthorModel authorModel : authors){
            authorModel.addBook(book);
        }
        book.setAuthors(authors);

        BookModel savedBook = bookRepository.save(book);

        return bookMapper.toBookResponseDTO(savedBook);
    }

    public List<BookResponseDTO> findAllBooks(){
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<BookResponseDTO> findBookById(Long id){
        return bookRepository.findById(id)
                .map(bookMapper::toBookResponseDTO);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}
