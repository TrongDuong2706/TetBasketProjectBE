package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.PermissionRequest;
import com.hanu.isd.dto.response.PermissionResponse;
import com.hanu.isd.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
