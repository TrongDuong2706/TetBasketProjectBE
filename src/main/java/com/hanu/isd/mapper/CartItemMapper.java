package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.CartItemRequest;
import com.hanu.isd.dto.response.CartItemResponse;
import com.hanu.isd.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest request);

    @Mapping(source = "basket.id", target = "basketId")
    @Mapping(source = "basket.name", target = "name")
    @Mapping(source = "basket.price", target = "price")
    CartItemResponse toCartItemResponse(CartItem cartItem);
}
