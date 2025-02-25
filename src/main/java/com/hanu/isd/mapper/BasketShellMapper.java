package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.BasketShellRequest;
import com.hanu.isd.dto.response.BasketShellResponse;
import com.hanu.isd.entity.BasketShell;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasketShellMapper {
    BasketShell toBasketShell(BasketShellRequest request);
    BasketShellResponse toBasketShellResponse(BasketShell basketShell);

}
