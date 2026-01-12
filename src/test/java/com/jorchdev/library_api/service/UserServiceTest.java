package com.jorchdev.library_api.service;

import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.mappers.UserMapper;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import com.jorchdev.library_api.repositories.UserRepository;
import com.jorchdev.library_api.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void findUser_shouldReturnUserResponse() {
        User user = new User();
        user.setId(1L);
        user.setName("Jorch");
        user.setEmail("jorgecoteralopez@gmail.com");

        UserResponse response = new UserResponse(
                1L, "Jorch", "jorgecoteralopez@gmail.com", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.findUser(1L);

        assertThat(result).isEqualTo(response);

        verify(userRepository).findById(1L);
        verify(userMapper).toResponse(user);
    }


    @Test
    void deleteUser_shouldDeleteExistingUser() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }


    @Test
    void updateUser_shouldUpdateAndReturnUserResponse() {
        User user = new User();
        user.setId(1L);
        user.setName("Jorch");

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("JorchDev");

        UserResponse response = new UserResponse(
                1L, "JorchDev", "jorgecoteralopez@gmail.com", null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.updateUser(updateRequest, 1L);

        verify(userRepository).findById(1L);
        verify(userMapper).toUpdate(updateRequest, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(user);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUser(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findById(1L);
    }

    @Test
    void updateUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserUpdateRequest request = new UserUpdateRequest();

        assertThatThrownBy(() -> userService.updateUser(request, 1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository).findById(1L);
    }
}
