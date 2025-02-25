package com.hanu.isd.mapper;

import com.hanu.isd.dto.response.RoleResponse;
import com.hanu.isd.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.hanu.isd.dto.request.RoleRequest;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
