package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.mappers.AuthMapper;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthMapperTest {

    private final AuthMapper authMapper = new AuthMapper();

    @Test
    void toEntity_shouldMapRegisterRequestAndSetDefaultRole() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Jorch");
        request.setEmail("jorgecoteralopez@gmail.com");
        request.setPassword("password123");

        User user = authMapper.toEntity(request);

        assertThat(user.getName()).isEqualTo("Jorch");
        assertThat(user.getEmail()).isEqualTo("jorgecoteralopez@gmail.com");
        assertThat(user.getRole()).isEqualTo(Roles.READER);

        assertThat(user.getId()).isNull();
        assertThat(user.getPassword()).isNull();
    }
}
