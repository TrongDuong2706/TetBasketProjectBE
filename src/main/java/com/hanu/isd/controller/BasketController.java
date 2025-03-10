package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.BasketRequest;
import com.hanu.isd.dto.response.BasketResponse;
import com.hanu.isd.dto.response.PaginatedResponse;
import com.hanu.isd.service.BasketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/basket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BasketController {
    BasketService basketService;
    @PostMapping
    public ApiResponse<BasketResponse> createBasket(@RequestPart("basket") BasketRequest request,
                                                    @RequestPart("images") List<MultipartFile> files
                                                   ){
    var basket = basketService.createBasket(request,files);
    return ApiResponse.<BasketResponse>builder()
            .result(basket)
            .build();
    }
    @GetMapping
    public ApiResponse<PaginatedResponse<BasketResponse>> getALlBasket(
            @RequestParam(defaultValue = "1")int page,
            @RequestParam(defaultValue = "5") int size){
        int adjustedPage = Math.max(page - 1, 0);
        var basket = basketService.getAllBasket(adjustedPage, size);
        return ApiResponse.<PaginatedResponse<BasketResponse>>builder()
                .result(basket)
                .build();
    }
    @PutMapping("/{basketId}")
    public ApiResponse<BasketResponse> updateBasket(
            @PathVariable Long basketId,
            @RequestPart("basket") BasketRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> files,
            @RequestParam(value = "keepExistingImages", required = false, defaultValue = "false") boolean keepExistingImages)
    {
        return ApiResponse.<BasketResponse>builder()
                .result(basketService.updateBasket(request, files, basketId, keepExistingImages))
                .build();
    }
    @GetMapping("/{basketId}")
    public ApiResponse<BasketResponse> getOneBasket(@PathVariable Long basketId){
        return ApiResponse.<BasketResponse>builder()
                .result(basketService.getOneBasket(basketId))
                .build();
    }
    @DeleteMapping("/{basketId}")
    public ApiResponse<String> deleteBasket(@PathVariable Long basketId){
        return ApiResponse.<String>builder()
                .result(basketService.deleteBasket(basketId))
                .build();
    }
    @GetMapping("/filter/basket")
    public ApiResponse<PaginatedResponse<BasketResponse>> getFilterBasket(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) int status
    ){
        int adjustedPage = Math.max(page - 1, 0);
        var baskets = basketService.getFilterBasket(adjustedPage, size, name, minPrice, maxPrice, categoryId, status);
        return ApiResponse.<PaginatedResponse<BasketResponse>>builder()
                .result(baskets)
                .build();
    }
    @GetMapping("/getAllBasket")
    public ApiResponse<List<BasketResponse>> getAllsBasket(){
        return ApiResponse.<List<BasketResponse>>builder()
                .result(basketService.getAllBasket())
                .build();
    }

    @GetMapping("/getBasketByName")
    public ApiResponse<PaginatedResponse<BasketResponse>> getBasketByName(@RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "4") int size,
                                                                          @RequestParam(required = false) String name){
        int adjustedPage = Math.max(page - 1, 0);
        var baskets = basketService.getAllBasketByName(adjustedPage, size, name);
        return ApiResponse.<PaginatedResponse<BasketResponse>>builder()
                .result(baskets)
                .build();
    }


}
