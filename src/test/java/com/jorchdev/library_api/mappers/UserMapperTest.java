package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.mappers.UserMapper;
import com.jorchdev.library_api.mappers.UserMapperImpl;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void toResponse_shouldMapUserToUserResponse() {
        User user = new User();
        user.setName("Jorch");
        user.setEmail("jorgecoteralopez@gmail.com");
        user.setRole(Roles.READER);

        UserResponse response = userMapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(user.getName());
        assertThat(response.email()).isEqualTo(user.getEmail());
    }

    @Test
    void toUpdate_shouldUpdateOnlyNonNullFields() {
        User user = new User();
        user.setName("Jorch");
        user.setEmail("jorgecoteralopez@gmail.com");

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("JorchDev");

        userMapper.toUpdate(updateRequest, user);

        assertThat(user.getName()).isEqualTo("JorchDev");
        assertThat(user.getEmail()).isEqualTo("jorgecoteralopez@gmail.com");
    }

    @Test
    void toUpdate_shouldUpdateMultipleFields() {
        User user = new User();
        user.setName("Old Name");
        user.setEmail("old@email.com");

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("New Name");
        updateRequest.setEmail("new@email.com");

        userMapper.toUpdate(updateRequest, user);

        assertThat(user.getName()).isEqualTo("New Name");
        assertThat(user.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    void toUpdate_shouldNotModifyEntityWhenUpdateRequestIsEmpty() {
        User user = new User();
        user.setName("Jorch");
        user.setEmail("jorgecoteralopez@gmail.com");

        UserUpdateRequest updateRequest = new UserUpdateRequest();

        userMapper.toUpdate(updateRequest, user);

        assertThat(user.getName()).isEqualTo("Jorch");
        assertThat(user.getEmail()).isEqualTo("jorgecoteralopez@gmail.com");
    }

    @Test
    void toResponse_shouldReturnNullWhenUserIsNull() {
        UserResponse response = userMapper.toResponse(null);

        assertThat(response).isNull();
    }
}
