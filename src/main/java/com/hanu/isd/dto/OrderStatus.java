package com.hanu.isd.dto;

public enum OrderStatus {
    PENDING,    // Đơn hàng đang chờ xử lý
    CONFIRMED,  // Đơn hàng đã được xác nhận
    SHIPPED,    // Đơn hàng đã giao hàng
    DELIVERED,  // Đơn hàng đã được nhận
    CANCELED    // Đơn hàng đã bị hủy
}
