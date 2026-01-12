package com.jorchdev.library_api.controllers;

import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.services.UserService;
import com.jorchdev.library_api.utils.CurrentUserProvider;
import com.jorchdev.library_api.utils.DeleteAccountConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final CurrentUserProvider currentUserProvider;

    private static final Logger log =
            LoggerFactory.getLogger(UserController.class);

    public UserController (
            UserService userService,
            CurrentUserProvider currentUserProvider)
    {
        this.userService = userService;
        this.currentUserProvider = currentUserProvider;
    }


    @Operation(summary = "Endpoint para un get del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Envía un UserResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        if (authentication == null) {
            log.error("Authentication is NULL");

        } else {
            log.info("Authenticated user: {}", authentication.getName());
            log.info("Authorities: {}", authentication.getAuthorities());
        }

        return ResponseEntity.ok(
                userService.findUser(
                        currentUserProvider.getCurrentUser().getId()
                )
        );
    }


    @Operation(summary = "Endpoint con @PreAuthorize(hasRoles('LIBRARIAN')) para un get de cualquier usuario")
    @ApiResponse(responseCode = "200", description = "Envía un UserResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser (@PathVariable Long id){
        return ResponseEntity.ok(userService.findUser(id));
    }

    @Operation(summary = "Endpoint para un put del usuario autenticado")
    @ApiResponse(responseCode = "200", description = "Envía un UserResponse")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PutMapping
    public ResponseEntity<UserResponse> putMe (@RequestBody UserUpdateRequest updateRequest){

        return ResponseEntity.ok(
                userService.updateUser(
                        updateRequest,
                        currentUserProvider
                                .getCurrentUser()
                                .getId()
                )
        );
    }

    @Operation(summary = "Endpoint para un delete del usuario autenticado, recibe una Confirmation Phrase igual a: \"Im sure about deleting my account\"")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "400", description = "Confirmation Phrase does not match")
    @ApiResponse(responseCode = "401")
    @DeleteMapping("/me/{confirmation}")
    public ResponseEntity<Void> deleteMe (@PathVariable String confirmation){
        if (confirmation.equals(DeleteAccountConstants.CONFIRMATION_PHRASE)){
            userService.deleteUser(currentUserProvider.getCurrentUser().getId());
            return ResponseEntity.noContent().build();
        } else {
            throw new IllegalArgumentException("Confirmation Phrase does not match");
        }
    }

    @Operation(summary = "Endpoint con @PreAuthorize para un delete de cualquier usuario")
    @ApiResponse(responseCode = "204")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "401")
    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id){
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
