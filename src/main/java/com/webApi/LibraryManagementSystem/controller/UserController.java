package com.webApi.LibraryManagementSystem.controller;

import com.webApi.LibraryManagementSystem.dto.UserRequestDTO;
import com.webApi.LibraryManagementSystem.dto.UserResponseDTO;
import com.webApi.LibraryManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/post")
    public ResponseEntity<UserResponseDTO> postUser(@RequestBody UserRequestDTO userDetails){
        return new ResponseEntity<>(userService.createUser(userDetails), HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String memberId) {
        return new ResponseEntity<>(userService.getUserByMemberId(memberId), HttpStatus.ACCEPTED);
    }

    @PutMapping("/put/{memberId}")
    public ResponseEntity<UserResponseDTO> putUser(@PathVariable String memberId, @RequestBody UserRequestDTO userDetails){
        try{
            return new ResponseEntity<>(userService.updateUser(memberId, userDetails), HttpStatus.CREATED);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{memberId}")
    public void deleteUser(@PathVariable String memberId){
        userService.deleteUser(memberId);
    }
}
