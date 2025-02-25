package com.hanu.isd.service;

import com.hanu.isd.dto.OrderStatus;
import com.hanu.isd.dto.email.request.Recipient;
import com.hanu.isd.dto.email.request.SendEmailRequest;
import com.hanu.isd.dto.request.ApplyVoucherRequest;
import com.hanu.isd.dto.request.OrderRequest;
import com.hanu.isd.dto.response.ApplyVoucherResponse;
import com.hanu.isd.dto.response.OrderResponse;
import com.hanu.isd.entity.Order;
import com.hanu.isd.entity.Voucher;
import com.hanu.isd.repository.OrderRepository;
import com.hanu.isd.repository.VoucherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;




@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    VoucherRepository voucherRepository;
    VoucherService voucherService;
    EmailService emailService;

    public OrderResponse createOrder(OrderRequest request) {
        Double totalAmount = request.getTotalAmount();
        Double shippingFee = 30000.0; // Mặc định phí ship 30K
        Double discountAmount = 0.0;
        Double finalAmount = totalAmount + shippingFee; // Cộng phí ship trước khi áp dụng voucher
        String appliedVoucherCode = null;

        log.info("Bắt đầu xử lý đơn hàng cho userId: {}", request.getUserId());

        // Kiểm tra nếu người dùng có nhập voucher
        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            log.info("Người dùng nhập voucher: {}", request.getVoucherCode());

            // Gọi applyVoucher từ VoucherService và truyền phí ship vào
            ApplyVoucherResponse voucherResponse = voucherService.applyVoucher(
                    new ApplyVoucherRequest(request.getVoucherCode(), totalAmount),
                    shippingFee // Truyền phí ship để tính tổng đúng
            );

            if (voucherResponse.getDiscountAmount() > 0) {
                discountAmount = voucherResponse.getDiscountAmount();
                finalAmount = voucherResponse.getNewOrderAmount(); // Giá trị sau khi giảm
                appliedVoucherCode = voucherResponse.getVoucherCode();
                log.info("Voucher hợp lệ. Giảm giá: {}, Tổng tiền mới (sau ship): {}", discountAmount, finalAmount);
            } else {
                log.warn("Voucher không hợp lệ: {}", voucherResponse.getMessage());
            }
        }

        log.info("Tạo đơn hàng với tổng tiền cuối cùng: {}", finalAmount);

        // Tạo đối tượng Order
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setFinalAmount(finalAmount); // Tổng cuối cùng đã tính ship + giảm giá
        order.setVoucherCode(appliedVoucherCode);
        order.setDiscountAmount(discountAmount);
        order.setShippingFee(shippingFee);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(new Date());
        order.setFullName(request.getFullName());
        order.setEmail(request.getEmail());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setAddress(request.getAddress());

        // Lưu vào database
        order = orderRepository.save(order);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(Recipient.builder()
                        .name(request.getFullName())
                        .email(request.getEmail())
                        .build())
                .subject("Thanks for order")
                .htmlContent("<p>Cảm ơn </p>")
                .build();
        emailService.sendEmail(sendEmailRequest);


        // Trả về response
        return new OrderResponse(
                order.getId(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getFinalAmount(),
                order.getVoucherCode(),
                order.getDiscountAmount(),
                order.getShippingFee(),
                order.getOrderStatus().toString(),
                order.getOrderDate(),
                order.getFullName(),
                order.getEmail(),
                order.getPhoneNumber(),
                order.getAddress()
        );
    }



}
