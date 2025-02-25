package com.hanu.isd.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String voucherCode;

    @Column()
    Double discountPercentage;

    @Column()
    String name;

    @Column()
    Date expiryDate;

    @Column()
    String status;

    @Column()
    Double minPurchaseAmount; // Điều kiện mua tối thiểu

    @Column()
    Double fixedDiscount; // Giảm giá cố định

    @Column()
    Integer quantity; // Số lượng voucher còn lại
    @Column()
    String image; //Ảnh cho voucher
}
