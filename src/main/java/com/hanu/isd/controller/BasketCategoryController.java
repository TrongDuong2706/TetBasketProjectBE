package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.BasketCategoryRequest;
import com.hanu.isd.dto.response.BasketCategoryResponse;
import com.hanu.isd.service.BasketCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basketcategory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BasketCategoryController {
    BasketCategoryService basketCategoryService;

    @PostMapping
    ApiResponse<BasketCategoryResponse> createBasketCategory(@RequestBody BasketCategoryRequest request){
        return ApiResponse.<BasketCategoryResponse>builder()
                .result(basketCategoryService.createBasketCategory(request))
                .build();
    }
    @GetMapping
    ApiResponse<List<BasketCategoryResponse>> getAllBasketCategory(){
        return ApiResponse.<List<BasketCategoryResponse>>builder()
                .result(basketCategoryService.getAll())
                .build();
    }
    @GetMapping("/{basketCategoryId}")
    ApiResponse<BasketCategoryResponse> getOneBasketCategory(@PathVariable Long basketCategoryId){
        return ApiResponse.<BasketCategoryResponse>builder()
                .result(basketCategoryService.getOneBasketCategory(basketCategoryId))
                .build();
    }
    @PutMapping("/{basketCategoryId}")
    ApiResponse<BasketCategoryResponse> updateBasketCategory(@PathVariable Long basketCategoryId, @RequestBody BasketCategoryRequest request){
        return ApiResponse.<BasketCategoryResponse>builder()
                .result(basketCategoryService.updateBasketCategory(basketCategoryId, request))
                .build();
    }

    @DeleteMapping("/{basketCategoryId}")
    ApiResponse<String> deleteBasketCategory(@PathVariable Long basketCategoryId){
        return ApiResponse.<String>builder()
                .result(basketCategoryService.deleteBasketCategory(basketCategoryId))
                .build();
    }

}
