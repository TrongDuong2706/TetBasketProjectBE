package com.hanu.isd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    BASKET_CATEGORY_NOT_EXISTED(1009, "Basket category not existed", HttpStatus.BAD_REQUEST),
    ITEM_NOT_EXISTED(1010, "Item not existed", HttpStatus.BAD_REQUEST),
    BASKET_NOT_EXISTED(1011, "Basket not existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_EXISTED(1012, "Voucher not existed", HttpStatus.BAD_REQUEST),
    CANNOT_SEND_EMAIL(1013, "Cannot send email", HttpStatus.BAD_REQUEST),
    BASKET_SHELL_NOT_EXISTED(1014, "Basket Shell Not existed", HttpStatus.BAD_REQUEST),
    ORDER_NOT_EXISTED(1015, "Order Not existed", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_CRITERIA(1016, "Không đủ điều kiện sử dụng voucher", HttpStatus.BAD_REQUEST),
    CART_NOT_FOUND(1017, "Không tìm thấy giỏ hàng", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(1018, "Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_FOUND(1019, "Không tìm thấy voucher", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1020, "Không thể cập nhập trạng thái", HttpStatus.BAD_REQUEST),
    GREATER_THAN_QUANTITY(1020, "Số lượng hàng có sẵn không đủ", HttpStatus.BAD_REQUEST),
    CANCEL_STATUS(1021, "Chỉ có thể hủy đơn hàng nếu chưa được xác nhận", HttpStatus.BAD_REQUEST),
    BASKET_ALREADY_DELETED(1022, "Không thể xóa giỏ hàng", HttpStatus.BAD_REQUEST),
    VOUCHER_INVALID_STATUS(1023, "Voucher đã bị vô hiệu hóa", HttpStatus.BAD_REQUEST),
    VOUCHER_EXPIRED(1024, "Voucher quá hạn sử dụng", HttpStatus.BAD_REQUEST),
    VOUCHER_OUT_OF_STOCK(1023, "Đã hết số lượng voucher", HttpStatus.BAD_REQUEST),
    VOUCHER_EXISTED(1024, "Mã voucher đã tồn tại", HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
