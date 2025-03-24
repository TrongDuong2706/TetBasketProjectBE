package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.CartItemRequest;
import com.hanu.isd.dto.response.CartItemResponse;
import com.hanu.isd.entity.BasketImage;
import com.hanu.isd.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")

public interface CartItemMapper {
    CartItem toCartItem(CartItemRequest request);

    @Mapping(source = "basket.id", target = "basketId")
    @Mapping(source = "basket.name", target = "name")
    @Mapping(source = "basket.price", target = "price")
    @Mapping(source = "basket.images", target = "imageUrls", qualifiedByName = "mapImagesToUrls") // Gọi phương thức tùy chỉnh
    CartItemResponse toCartItemResponse(CartItem cartItem);

    // Phương thức chuyển đổi danh sách ảnh
    @Named("mapImagesToUrls")
    static List<String> mapImagesToUrls(List<BasketImage> images) {
        if (images == null || images.isEmpty()) {
            return List.of(); // Trả về danh sách rỗng nếu không có ảnh
        }
        return images.stream()
                .map(BasketImage::getImageUrl) // Lấy thuộc tính URL từ mỗi ảnh
                .toList();
    }
}
