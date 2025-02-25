package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.BasketCategoryRequest;
import com.hanu.isd.dto.response.BasketCategoryResponse;
import com.hanu.isd.entity.BasketCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BasketCategoryMapper {
    BasketCategory toBasketCategory(BasketCategoryRequest request);
    BasketCategoryResponse toBasketCategoryResponse(BasketCategory basketCategory);
}
