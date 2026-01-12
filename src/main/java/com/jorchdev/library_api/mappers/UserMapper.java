package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.update.UserUpdateRequest;
import com.jorchdev.library_api.dto.response.UserResponse;
import com.jorchdev.library_api.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;

@Component
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public abstract class UserMapper {
    public abstract UserResponse toResponse(User user);

    public abstract void toUpdate(
            UserUpdateRequest dto,
            @MappingTarget User entity
    );
}
