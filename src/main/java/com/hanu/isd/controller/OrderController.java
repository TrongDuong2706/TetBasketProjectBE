package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.OrderRequest;
import com.hanu.isd.dto.response.OrderResponse;
import com.hanu.isd.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
