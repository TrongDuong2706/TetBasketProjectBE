package com.hanu.isd.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRequest {
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
