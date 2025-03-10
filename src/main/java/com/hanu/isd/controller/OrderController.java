package com.hanu.isd.controller;

import com.cloudinary.Api;
import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.OrderRequest;
import com.hanu.isd.dto.response.OrderItemResponse;
import com.hanu.isd.dto.response.OrderResponse;
import com.hanu.isd.dto.response.PaginatedResponse;
import com.hanu.isd.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderController {
    OrderService orderService;
    @PostMapping()
    public ApiResponse<OrderResponse> createOrder(@RequestBody  OrderRequest orderRequest){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(orderRequest))
                .build();
    }
    @GetMapping()
    public ApiResponse<PaginatedResponse<OrderResponse>> getAllOrder( @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "4") int size,
                                                                      @RequestParam(required = false) String fullName,
                                                                      @RequestParam(required = false) BigDecimal minPrice,
                                                                      @RequestParam(required = false) BigDecimal maxPrice){

        return ApiResponse.<PaginatedResponse<OrderResponse>>builder()
                .result(orderService.getAllOrder(page,size, fullName, minPrice, maxPrice))
                .build();
    }
    @GetMapping("/{orderId}")
    public ApiResponse<List<OrderItemResponse>> getAllItemByOrderId(@PathVariable Long orderId){
        return ApiResponse.<List<OrderItemResponse>>builder()
                .result(orderService.getAllItemByOrderId(orderId))
                .build();
    }

    @GetMapping("/order-detail/{orderId}")
    public ApiResponse<OrderResponse> getOneOrderById(@PathVariable Long orderId){
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.getOneOrder(orderId))
                .build();
    }
}
