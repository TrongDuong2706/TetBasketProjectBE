package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplyVoucherResponse {
    String voucherCode;
    String message;
    Double discountAmount;
    Double newOrderAmount;
}
