package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.VoucherRequest;
import com.hanu.isd.dto.response.VoucherResponse;
import com.hanu.isd.entity.Voucher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest request);
    VoucherResponse toVoucherResponse(Voucher voucher);
}
