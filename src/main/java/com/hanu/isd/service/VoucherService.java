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
        Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(request.getVoucherCode());
        if (voucherOpt.isPresent()) {
            throw new AppException(ErrorCode.VOUCHER_EXISTED);
        }
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
        // Kiểm tra voucherCode có tồn tại không
        Optional<Voucher> voucherOpt = voucherRepository.findByVoucherCode(request.getVoucherCode());

        if (voucherOpt.isEmpty()) {
            // Ném ra AppException nếu voucherCode không tồn tại
            throw new AppException(ErrorCode.VOUCHER_NOT_FOUND);
        }

        Voucher voucher = voucherOpt.get();

        // Kiểm tra trạng thái voucher
        if (!"ACTIVE".equalsIgnoreCase(voucher.getStatus())) {
            // Ném ra AppException nếu voucher không hợp lệ hoặc bị vô hiệu hóa
            throw new AppException(ErrorCode.VOUCHER_INVALID_STATUS);
        }

        // Kiểm tra ngày hết hạn của voucher
        if (voucher.getExpiryDate().before(new Date())) {
            // Ném ra AppException nếu voucher đã hết hạn
            throw new AppException(ErrorCode.VOUCHER_EXPIRED);
        }

        // Kiểm tra số lượng voucher
        if (voucher.getQuantity() <= 0) {
            // Ném ra AppException nếu voucher đã hết số lượng
            throw new AppException(ErrorCode.VOUCHER_OUT_OF_STOCK);
        }

        // Kiểm tra giá trị đơn hàng
        if (request.getOrderAmount() < voucher.getMinPurchaseAmount()) {
            throw new AppException(ErrorCode.VOUCHER_NOT_CRITERIA);
        }

        // Tính toán số tiền giảm giá
        Double discountAmount = 0.0;
        if (voucher.getFixedDiscount() != null && voucher.getFixedDiscount() > 0) {
            discountAmount = voucher.getFixedDiscount();
        } else if (voucher.getDiscountPercentage() != null && voucher.getDiscountPercentage() > 0) {
            discountAmount = request.getOrderAmount() * (voucher.getDiscountPercentage() / 100.0);
        }

        // Làm tròn số tiền giảm giá
        discountAmount = Math.round(discountAmount * 100.0) / 100.0;
        discountAmount = Math.min(discountAmount, request.getOrderAmount());
        Double newOrderAmount = request.getOrderAmount() - discountAmount;

        // Cập nhật lại số lượng voucher
        voucher.setQuantity(voucher.getQuantity() - 1);
        voucherRepository.save(voucher);

        // Trả về kết quả sau khi áp dụng voucher
        return ApplyVoucherResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .message("Voucher áp dụng thành công.")
                .discountAmount(discountAmount)
                .newOrderAmount(newOrderAmount)
                .build();
    }






}

