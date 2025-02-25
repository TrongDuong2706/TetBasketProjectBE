package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.BasketCategoryRequest;
import com.hanu.isd.dto.request.BasketShellRequest;
import com.hanu.isd.dto.response.BasketCategoryResponse;
import com.hanu.isd.dto.response.BasketShellResponse;
import com.hanu.isd.service.BasketShellService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basketshell")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BasketShellController {
    BasketShellService basketShellService;
    @PostMapping
    ApiResponse<BasketShellResponse> createBasketCategory(@RequestBody BasketShellRequest request){
        return ApiResponse.<BasketShellResponse>builder()
                .result(basketShellService.createBasketShell(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<BasketShellResponse>> getAllBasketCategory(){
        return ApiResponse.<List<BasketShellResponse>>builder()
                .result(basketShellService.getAllBasketShell())
                .build();
    }
    @GetMapping("/{basketShellId}")
    ApiResponse<BasketShellResponse> getOneBasketCategory(@PathVariable Long basketShellId){
        return ApiResponse.<BasketShellResponse>builder()
                .result(basketShellService.getOneBasketShell(basketShellId))
                .build();
    }
}
