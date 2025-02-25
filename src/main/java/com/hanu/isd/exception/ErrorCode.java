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
