package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemResponse {
    Long id;  // ID của sản phẩm trong giỏ hàng
    Long basketId;  // ID của sản phẩm
    String name;  // Tên sản phẩm
    Double price;  // Giá sản phẩm
    int quantity;  // Số lượng sản phẩm trong giỏ hàng
    List<String> imageUrls;
}
