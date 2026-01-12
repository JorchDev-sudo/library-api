package com.jorchdev.library_api.mappers;

import com.jorchdev.library_api.dto.request.RegisterRequest;
import com.jorchdev.library_api.models.User;
import com.jorchdev.library_api.models.enums.Roles;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public User toEntity (RegisterRequest request){
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(Roles.READER);

        return user;
    }
}
