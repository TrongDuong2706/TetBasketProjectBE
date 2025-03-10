package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemResponse {
    Long id;
    Long basketId;
    int quantity;
    String description;
    String name;
    double price;
    List<BasketImageResponse> images;

}
