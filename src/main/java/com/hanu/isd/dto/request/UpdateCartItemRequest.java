package com.hanu.isd.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCartItemRequest {
    String userId;
    Long basketId;
    int quantityChange;
}
