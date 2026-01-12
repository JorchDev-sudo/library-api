package com.jorchdev.library_api.services;

import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.mappers.UserMapper;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService (UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponse findUser (Long id){
        User user = userRepository.findById(id)
                         .orElseThrow(EntityNotFoundException::new);

        return userMapper.toResponse(user);
    }

    public void deleteUser (Long id){
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User wit id: " + id + "not found")));
    }

    public UserResponse updateUser (UserUpdateRequest updateRequest, Long userId){
        User user = userRepository.findById(userId)
                        .orElseThrow(EntityNotFoundException::new);

        userMapper.toUpdate(updateRequest, user);
        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
