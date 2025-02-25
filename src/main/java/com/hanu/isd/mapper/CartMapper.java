package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.CartRequest;
import com.hanu.isd.dto.response.CartResponse;
import com.hanu.isd.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartRequest request);
    CartResponse toCartResponse(Cart cart);
}
