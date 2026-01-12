package com.jorchdev.library_api.services;

import com.jorchdev.library_api.dto.request.create.BookRequest;
import com.jorchdev.library_api.dto.request.update.BookUpdateRequest;
import com.jorchdev.library_api.dto.response.BookResponse;
import com.jorchdev.library_api.mappers.BookMapper;
import com.jorchdev.library_api.models.Author;
import com.jorchdev.library_api.models.Book;
import com.jorchdev.library_api.repositories.BookRepository;
import com.jorchdev.library_api.utils.UtilService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private  final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorService authorService;
    private final UtilService utilService;

    public BookService(
            BookRepository bookRepository,
            BookMapper bookMapper,
            AuthorService authorService,
            UtilService utilService)
    {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorService = authorService;
        this.utilService = utilService;
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        List<Author> authors = new ArrayList<>(request.getAuthorsIds().size());

        for (long id : request.getAuthorsIds()){
            authors.add(utilService.authorHelper(id));
        }

        if (authors.size() != request.getAuthorsIds().size()) {
            throw new EntityNotFoundException("Author not found");
        }

        Book newBook = bookMapper.toEntity(request, authors);
        Book savedBook = bookRepository.save(newBook);

        for (long id : request.getAuthorsIds()){
            authorService.addBook(id, newBook.getId());
        }

        return bookMapper.toResponse(savedBook);
    }

    public List<BookResponse> findBooks (
            long authorId,
            int page,
            int size,
            String direction)
    {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("publicationDate").ascending()
                : Sort.by("publicationDate").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Book> bookPage;

        bookPage = bookRepository.findByAuthors(utilService.authorHelper(authorId), pageable);

        return bookPage
                .stream()
                .map(bookMapper::toResponse).toList();
    }

    public BookResponse findBook (long id){
        return bookMapper.toResponse(bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));
    }

    public BookResponse updateBook (BookUpdateRequest updateRequest, long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        bookMapper.toUpdate(updateRequest, book);

        bookRepository.save(book);
        return bookMapper.toResponse(book);
    }

    public void deleteBook(Long id){
        bookRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new);

        bookRepository.deleteById(id);
    }
}
