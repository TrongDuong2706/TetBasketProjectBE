package com.hanu.isd.controller;

import com.hanu.isd.dto.request.ApiResponse;
import com.hanu.isd.dto.request.ApplyVoucherRequest;
import com.hanu.isd.dto.request.VoucherRequest;
import com.hanu.isd.dto.response.ApplyVoucherResponse;
import com.hanu.isd.dto.response.VoucherResponse;
import com.hanu.isd.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherController {
    VoucherService voucherService;
    @PostMapping()
    public ApiResponse<VoucherResponse> createVoucher(@RequestBody VoucherRequest voucherRequest){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.createVoucher(voucherRequest))
                .build();
    }
    @GetMapping()
    public ApiResponse<List<VoucherResponse>> getAllVoucher(){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherService.getAllVoucher())
                .build();
    }
    @GetMapping("/{voucherId}")
    ApiResponse<VoucherResponse> getOneItem(@PathVariable Long voucherId){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.getOneVoucher(voucherId))
                .build();
    }
    @PostMapping("/apply")
    ApiResponse<ApplyVoucherResponse> applyVoucher(@RequestBody ApplyVoucherRequest applyVoucherRequest) {
        return ApiResponse.<ApplyVoucherResponse>builder()
                .result(voucherService.applyVoucher(applyVoucherRequest)) // Truyền thêm shippingFee
                .build();
    }
    @PutMapping("/{voucherId}")
    ApiResponse<VoucherResponse> updateVoucher(@RequestBody VoucherRequest request, @PathVariable Long voucherId){
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherService.updateVoucher(request, voucherId))
                .build();
    }
    @DeleteMapping("/{voucherId}")
    ApiResponse<String> deleteVoucher(@PathVariable Long voucherId){
        return ApiResponse.<String>builder()
                .result(voucherService.deleteVoucher(voucherId))
                .build();
    }

}
