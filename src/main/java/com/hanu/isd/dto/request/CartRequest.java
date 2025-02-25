package com.hanu.isd.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {
    String userId;  // ID của người dùng
    Long basketId;  // ID của sản phẩm cần thêm
    int quantity;   // Số lượng cần thêm vào giỏ hàng
}
