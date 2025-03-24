package com.hanu.isd.service;

import com.hanu.isd.dto.request.ApplyVoucherRequest;
import com.hanu.isd.dto.request.VoucherRequest;
import com.hanu.isd.dto.response.ApplyVoucherResponse;
import com.hanu.isd.dto.response.VoucherResponse;
import com.hanu.isd.entity.Voucher;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.mapper.VoucherMapper;
import com.hanu.isd.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherService {
    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;

    //Create Voucher
    public VoucherResponse createVoucher(VoucherRequest request){
        Voucher voucher = voucherMapper.toVoucher(request);
        voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(voucher);
    }

    public VoucherResponse updateVoucher(VoucherRequest request,Long voucherId ){
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucherMapper.updateVoucher(voucher,request);
        voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(voucher);
    }
    //Get All Voucher
    public List<VoucherResponse> getAllVoucher(){
        var voucher = voucherRepository.findAll();
        return voucher.stream().map(voucherMapper::toVoucherResponse).toList();
    }
    //Get One Voucher
    public VoucherResponse getOneVoucher(Long voucherId){
        var voucher = voucherRepository.findById(voucherId).orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_EXISTED));
        return voucherMapper.toVoucherResponse(voucher);
    }

    public String deleteVoucher(Long voucherId){

        if (!voucherRepository.existsById(voucherId)) {
            throw new AppException(ErrorCode.VOUCHER_NOT_EXISTED);
        }
        voucherRepository.deleteById(voucherId);
        return "xóa thành công";
    }
    //Apply Voucher
    public ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request) {
        Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(request.getVoucherCode());

        if (voucherOpt.isEmpty()) {
            return ApplyVoucherResponse.builder()
                    .voucherCode(request.getVoucherCode())
                    .message("Voucher không tồn tại.")
                    .discountAmount(0.0)
                    .newOrderAmount(request.getOrderAmount())
                    .build();
        }

        Voucher voucher = voucherOpt.get();

        if (!"ACTIVE".equalsIgnoreCase(voucher.getStatus())) {
            return ApplyVoucherResponse.builder()
                    .voucherCode(voucher.getVoucherCode())
                    .message("Voucher không hợp lệ hoặc đã bị vô hiệu hóa.")
                    .discountAmount(0.0)
                    .newOrderAmount(request.getOrderAmount())
                    .build();
        }

        if (voucher.getExpiryDate().before(new Date())) {
            return ApplyVoucherResponse.builder()
                    .voucherCode(voucher.getVoucherCode())
                    .message("Voucher đã hết hạn.")
                    .discountAmount(0.0)
                    .newOrderAmount(request.getOrderAmount())
                    .build();
        }

        if (voucher.getQuantity() <= 0) {
            return ApplyVoucherResponse.builder()
                    .voucherCode(voucher.getVoucherCode())
                    .message("Voucher đã hết số lượng.")
                    .discountAmount(0.0)
                    .newOrderAmount(request.getOrderAmount())
                    .build();
        }

        if (request.getOrderAmount() < voucher.getMinPurchaseAmount()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_CRITERIA);
        }

        Double discountAmount = 0.0;
        if (voucher.getFixedDiscount() != null && voucher.getFixedDiscount() > 0) {
            discountAmount = voucher.getFixedDiscount();
        } else if (voucher.getDiscountPercentage() != null && voucher.getDiscountPercentage() > 0) {
            discountAmount = request.getOrderAmount() * (voucher.getDiscountPercentage() / 100.0);
        }

        discountAmount = Math.round(discountAmount * 100.0) / 100.0;
        discountAmount = Math.min(discountAmount, request.getOrderAmount());
        Double newOrderAmount = request.getOrderAmount() - discountAmount;

        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);

        return ApplyVoucherResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .message("Voucher áp dụng thành công.")
                .discountAmount(discountAmount)
                .newOrderAmount(newOrderAmount)
                .build();
    }




}

