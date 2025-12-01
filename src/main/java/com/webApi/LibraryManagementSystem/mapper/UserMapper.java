package com.webApi.LibraryManagementSystem.mapper;

import com.webApi.LibraryManagementSystem.dto.UserResponseDTO;
import com.webApi.LibraryManagementSystem.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toUserResponseDTO(UserModel userModel){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(userModel.getId());
        dto.setName(userModel.getName());
        dto.setMemberId(userModel.getMemberId());

        return dto;
    }


}
