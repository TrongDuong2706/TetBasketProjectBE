package com.hanu.isd.mapper;

import com.hanu.isd.dto.request.BasketRequest;
import com.hanu.isd.dto.request.VoucherRequest;
import com.hanu.isd.dto.response.VoucherResponse;
import com.hanu.isd.entity.Basket;
import com.hanu.isd.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest request);
    VoucherResponse toVoucherResponse(Voucher voucher);

    void updateVoucher(@MappingTarget Voucher voucher, VoucherRequest request);

}
