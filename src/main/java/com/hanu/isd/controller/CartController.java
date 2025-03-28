package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.CartRequest;
import com.hanu.isd.dto.request.UpdateCartItemRequest;
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
    @GetMapping("/{userId}")
    public ApiResponse<List<CartItemResponse>> getAddCartItem(@PathVariable String userId){
        return ApiResponse.<List<CartItemResponse>>builder()
                .result(cartService.getCartItemsByUserId(userId))
                .build();
    }
    @PutMapping()
    public ApiResponse<CartResponse> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest request) {
        return ApiResponse.<CartResponse>builder()
                .result(cartService.updateCartItemQuantity(request))
                .build();
    }
    @DeleteMapping("/{userId}/items/{basketId}")
    public ApiResponse<CartResponse> deleteCartItem(@PathVariable String userId, @PathVariable Long basketId){
        return ApiResponse.<CartResponse>builder()
                .result(cartService.removeCartItem(userId, basketId))
                .build();
    }

    @GetMapping("/count")
    public ApiResponse<Integer> countItemInCart(@RequestParam String userId){
        return ApiResponse.<Integer>builder()
                .result(cartService.countTotalItemsInCart(userId))
                .build();
    }
}
