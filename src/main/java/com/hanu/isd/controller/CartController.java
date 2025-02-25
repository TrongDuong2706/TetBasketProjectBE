package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.CartRequest;
import com.hanu.isd.dto.response.CartItemResponse;
import com.hanu.isd.dto.response.CartResponse;
import com.hanu.isd.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartController {
    CartService cartService;
    @PostMapping()
    public ApiResponse<CartResponse> addToCart(@RequestBody CartRequest request){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.addToCart(request))
                .build();
    }
    //Lấy tất cả sản phẩm trong giỏ hàng
    @GetMapping("/{cardId}")
    public ApiResponse<List<CartItemResponse>> getAddCartItem(@PathVariable Long cardId){
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.getCartItemsByCartId(cardId))
                .build();
    }
}
