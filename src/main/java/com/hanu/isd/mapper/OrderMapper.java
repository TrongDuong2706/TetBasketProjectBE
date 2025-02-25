package com.hanu.isd.mapper;

import com.hanu.isd.dto.OrderStatus;
import com.hanu.isd.dto.request.OrderRequest;
import com.hanu.isd.dto.response.OrderResponse;
import com.hanu.isd.entity.Order;
import org.mapstruct.*;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "shippingFee", constant = "30000.0") // Phí ship cố định
    Order toOrder(OrderRequest orderRequest);

    @Mapping(target = "orderStatus", source = "orderStatus", qualifiedByName = "mapOrderStatusToString")
    OrderResponse toOrderResponse(Order order);

    @Named("mapOrderStatusToString")
    default String mapOrderStatusToString(OrderStatus orderStatus) {
        return orderStatus != null ? orderStatus.toString() : "PENDING";
    }
}
