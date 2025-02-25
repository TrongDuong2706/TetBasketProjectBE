package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    Long id;
    String voucherCode;
    String name;
    Double discountPercentage;
    Double fixedDiscount;
    Date expiryDate;
    String status;
    Double minPurchaseAmount;
    Integer quantity;
    String image;
}
