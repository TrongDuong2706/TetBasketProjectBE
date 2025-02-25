package com.hanu.isd.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String userId;
    Double totalAmount;
    String voucherCode;
    String fullName;
    String email;
    String phoneNumber;
    String address;
}
