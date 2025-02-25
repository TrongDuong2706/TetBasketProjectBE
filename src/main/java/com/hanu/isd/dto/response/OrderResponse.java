package com.hanu.isd.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    Long orderId;
    String userId;
    Double totalAmount;
    Double finalAmount;
    String voucherCode;
    Double discountAmount;
    Double shippingFee;
    String orderStatus;
    Date orderDate;
    // Thông tin khách hàng
    String fullName;
    String email;
    String phoneNumber;
    String address;

}
