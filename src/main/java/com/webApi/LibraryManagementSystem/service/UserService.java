package com.webApi.LibraryManagementSystem.service;

import com.webApi.LibraryManagementSystem.dto.UserRequestDTO;
import com.webApi.LibraryManagementSystem.dto.UserResponseDTO;
import com.webApi.LibraryManagementSystem.mapper.UserMapper;
import com.webApi.LibraryManagementSystem.model.UserModel;
import com.webApi.LibraryManagementSystem.repository.LoanRepository;
import com.webApi.LibraryManagementSystem.repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, LoanRepository loanRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO){
        UserModel newUser = new UserModel();

        newUser.setName(userRequestDTO.getName());
        newUser.setMemberId(userRequestDTO.getMemberId());

        UserModel savedUser = userRepository.save(newUser);

        return userMapper.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserByMemberId(String memberId) {
        return userRepository.findByMemberId(memberId)
                .map(userMapper::toUserResponseDTO) // convierte UserModel → UserResponseDTO
                .orElseThrow(() -> new IllegalArgumentException("User wit id: " + memberId + "not found"));
    }

    public UserResponseDTO updateUser(String memberId, UserRequestDTO userDetails){
        UserModel newUser = userRepository.findByMemberId(memberId)
                .orElseThrow(()-> new IllegalArgumentException("User wit id: " + memberId + "not found"));

        newUser.setName(userDetails.getName());

        return userMapper.toUserResponseDTO(newUser);
    }

    public void deleteUser(String memberId){
        userRepository.delete(userRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("User wit id: " + memberId + "not found")));
    }
}
