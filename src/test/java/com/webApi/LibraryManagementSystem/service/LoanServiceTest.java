
package com.webApi.LibraryManagementSystem.service;

import com.webApi.LibraryManagementSystem.dto.LoanRequestDTO;
import com.webApi.LibraryManagementSystem.model.BookModel;
import com.webApi.LibraryManagementSystem.model.LoanModel;
import com.webApi.LibraryManagementSystem.model.UserModel;
import com.webApi.LibraryManagementSystem.repository.BookRepository;
import com.webApi.LibraryManagementSystem.repository.UserRepository;
import com.webApi.LibraryManagementSystem.repository.LoanRepository;
import com.webApi.LibraryManagementSystem.mapper.LoanMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    // 1. Simulación de las dependencias (Mocks)
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoanMapper loanMapper; // Simular el Mapper para simplificar

    // 2. Inyección de Mocks en el Servicio a probar
    @InjectMocks
    private LoanService loanService;

    // Datos de prueba
    private BookModel availableBook;
    private BookModel unavailableBook;
    private UserModel testUser;
    private LoanRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Inicializar datos antes de cada prueba
        availableBook = new BookModel("The Test Book", "12345", 2023, 5); // Stock 5
        availableBook.setId(1L);

        unavailableBook = new BookModel("No Stock", "67890", 2023, 0); // Stock 0
        unavailableBook.setId(2L);

        testUser = new UserModel("Test User", "U001");
        testUser.setId(10L);

        requestDTO = new LoanRequestDTO();
        requestDTO.setBookId(1L);
        requestDTO.setUserId(10L);
        requestDTO.setDueDate(LocalDateTime.now().plusDays(7));
    }

    @Test
    void createLoan_ShouldSucceedAndDecrementStock() {
        // Arrange (Preparación)
        // 1. Simular que los repositorios devuelven los objetos necesarios
        when(bookRepository.findById(1L)).thenReturn(Optional.of(availableBook));
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));

        // 2. Simular el guardado del préstamo (no importa el objeto devuelto aquí)
        when(loanRepository.save(any(LoanModel.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act (Ejecución)
        loanService.createLoan(requestDTO);

        // Assert (Verificación)
        // 1. Verificar que el stock se decrementó de 5 a 4
        assertEquals(4, availableBook.getStock());

        // 2. Verificar que el método save fue llamado en ambos repositorios
        verify(bookRepository, times(1)).save(availableBook);
        verify(loanRepository, times(1)).save(any(LoanModel.class));
    }

    @Test
    void createLoan_ShouldThrowExceptionWhenStockIsZero() {
        // Arrange (Preparación)
        requestDTO.setBookId(2L); // Usar el libro sin stock
        when(bookRepository.findById(2L)).thenReturn(Optional.of(unavailableBook));
        when(userRepository.findById(10L)).thenReturn(Optional.of(testUser));

        // Act & Assert (Ejecución y Verificación de Excepción)
        // Debe lanzar IllegalStateException (la excepción de negocio que definiste)
        assertThrows(IllegalStateException.class, () -> {
            loanService.createLoan(requestDTO);
        });

        // Verificación de No-Guardado (Rollback Implícito)
        // 1. Asegurar que el stock del libro no cambió
        assertEquals(0, unavailableBook.getStock());
        // 2. Asegurar que NINGÚN guardado ocurrió en los repositorios
        verify(loanRepository, never()).save(any(LoanModel.class));
        verify(bookRepository, never()).save(unavailableBook); // No se debe llamar save en el libro
    }
}
