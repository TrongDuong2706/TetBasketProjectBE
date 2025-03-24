package com.hanu.isd.entity;

import com.hanu.isd.dto.OrderStatus;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String userId; // ID của khách hàng (nếu có hệ thống user)

    @Column()
    Double totalAmount; // Tổng tiền đơn hàng trước khi áp dụng voucher

    @Column()
    Double finalAmount; // Tổng tiền sau khi áp dụng voucher

    @Column()
    String voucherCode; // Mã voucher được sử dụng (nếu có)

    @Column()
    Double discountAmount; // Số tiền giảm giá từ voucher

    @Column()
    Double shippingFee = 30000.0; // Phí vận chuyển cố định 30K

    @Column()
    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus; // Trạng thái đơn hàng

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    Date orderDate; // Ngày đặt hàng

    // Thêm thông tin khách hàng
    @Column()
    String fullName; // Họ và tên khách hàng

    @Column()
    String email; // Email khách hàng

    @Column()
    String phoneNumber; // Số điện thoại khách hàng

    @Column()
    String address; // Địa chỉ giao hàng

    @Column()
    String note; //ghi chú cho order
}
