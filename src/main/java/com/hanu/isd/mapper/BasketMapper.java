package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.BasketRequest;
import com.hanu.isd.dto.response.BasketResponse;
import com.hanu.isd.dto.response.ItemResponse;
import com.hanu.isd.entity.Basket;
import com.hanu.isd.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BasketMapper {
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "basketShellId", target = "basketShell.id")
    Basket toBasket(BasketRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "itemNames", expression = "java(mapItemsToNames(basket.getItems()))")
    @Mapping(source = "basketShell.id", target = "basketShellId")  // Thêm dòng này
    BasketResponse toBasketResponse(Basket basket);

    void updateBasket(@MappingTarget Basket basket, BasketRequest request);

    default Set<String> mapItemsToNames(Set<Item> items) {
        return items.stream()
                .map(Item::getName)
                .collect(Collectors.toSet());
    }
}

