package com.webApi.LibraryManagementSystem.controller;

import com.webApi.LibraryManagementSystem.dto.LoanRequestDTO;
import com.webApi.LibraryManagementSystem.dto.LoanResponseDTO;
import com.webApi.LibraryManagementSystem.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {
    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService){
        this.loanService = loanService;
    }

    //Post
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO requestDTO) {
        try {
            LoanResponseDTO newLoan = loanService.createLoan(requestDTO);
            return new ResponseEntity<>(newLoan, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<LoanResponseDTO> returnBook(@PathVariable Long loanId){
        try{
            LoanResponseDTO returnedLoan = loanService.returnBook(loanId);
            return ResponseEntity.ok(returnedLoan);
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();

        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
