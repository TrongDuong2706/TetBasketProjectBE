package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.UserUpdateRequest;
import com.hanu.isd.dto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.hanu.isd.dto.request.UserCreationRequest;
import com.hanu.isd.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
