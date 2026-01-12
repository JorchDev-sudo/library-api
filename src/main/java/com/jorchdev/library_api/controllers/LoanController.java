package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.dto.request.create.LoanRequest;
import com.jorchdev.library_api.dto.response.LoanResponse;
import com.jorchdev.library_api.services.LoanService;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanService loanService;
    private final CurrentUserProvider currentUserProvider;

    public LoanController(
            LoanService loanService,
            CurrentUserProvider currentUserProvider)
    {
        this.loanService = loanService;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "Endpoint para la creación de Loan")
    @ApiResponse(responseCode = "201", description = "Envía un LoanResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "409", description = "El libro no posee un stock valido para pedir Loan")
    @ApiResponse(responseCode = "401")
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody LoanRequest loanDetails) {
        try {
            LoanResponse newLoan = loanService.createLoan(loanDetails, currentUserProvider.getCurrentUser().getId());
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @Operation(summary = "Endpoint para la devolución de Loan")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "409", description = "La Loan ya ha sido devuelta")
    @ApiResponse(responseCode = "401")
    @PutMapping("/{id}/return")
    public ResponseEntity<Void> returnBook(@PathVariable Long id){
        try{
            loanService.returnBook(id);
            return ResponseEntity.ok(null);

        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();

        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Endpoint para un get paginado de las Loans del usuario autenticado")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401")
    @GetMapping
    public ResponseEntity<List<LoanResponse>> getLoans (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String direction)
    {
        return ResponseEntity.ok(loanService.findLoans(currentUserProvider.getCurrentUser().getId(), page, size, direction));
    }
}
