package com.hanu.isd.service;

import com.hanu.isd.dto.OrderStatus;
import com.hanu.isd.dto.email.request.Recipient;
import com.hanu.isd.dto.email.request.SendEmailRequest;
import com.hanu.isd.dto.request.ApplyVoucherRequest;
import com.hanu.isd.dto.request.OrderRequest;
import com.hanu.isd.dto.response.*;
import com.hanu.isd.entity.*;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    VoucherRepository voucherRepository;
    VoucherService voucherService;
    EmailService emailService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Double totalAmount = request.getTotalAmount();
        Double shippingFee = 30000.0;
        Double discountAmount = 0.0;
        Double finalAmount = totalAmount + shippingFee;
        String appliedVoucherCode = null;

        log.info("Bắt đầu xử lý đơn hàng cho userId: {}", request.getUserId());

        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            ApplyVoucherResponse voucherResponse = voucherService.applyVoucher(
                    new ApplyVoucherRequest(request.getVoucherCode(), totalAmount),
                    shippingFee
            );

            if (voucherResponse.getDiscountAmount() > 0) {
                discountAmount = voucherResponse.getDiscountAmount();
                finalAmount = voucherResponse.getNewOrderAmount();
                appliedVoucherCode = voucherResponse.getVoucherCode();
            }
        }

        // ✅ Lấy giỏ hàng của user
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // ✅ Tạo Order
        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(totalAmount)
                .finalAmount(finalAmount)
                .voucherCode(appliedVoucherCode)
                .discountAmount(discountAmount)
                .shippingFee(shippingFee)
                .orderStatus(OrderStatus.PENDING)
                .orderDate(new Date())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();

        order = orderRepository.save(order);

        // ✅ Chuyển CartItem sang OrderItem
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .basket(cartItem.getBasket())
                    .quantity(cartItem.getQuantity())
                    .priceAtOrderTime(cartItem.getBasket().getPrice())
                    .build();
            orderItemRepository.save(orderItem);
        }

        // ✅ Xóa cartItems trước khi xóa giỏ hàng
        log.info("Danh sách cartItems cần xóa: {}", cartItems);
        cartItemRepository.deleteAll(cartItems);
        cartItemRepository.flush();  // Đảm bảo xóa ngay lập tức

        // ✅ Xóa cart
        cart.getItems().clear();
        cartRepository.save(cart);
        cartRepository.delete(cart);

        // ✅ Gửi email xác nhận
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(Recipient.builder()
                        .name(request.getFullName())
                        .email(request.getEmail())
                        .build())
                .subject("Thanks for your order")
                .htmlContent("<p>Cảm ơn bạn đã đặt hàng!</p>")
                .build();
        emailService.sendEmail(sendEmailRequest);

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

    public PaginatedResponse<OrderResponse> getAllOrder(int page, int size,
                                                        String fullName, BigDecimal minPrice, BigDecimal maxPrice) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByFullNameAndPriceRange(fullName, minPrice, maxPrice, pageRequest);

        List<OrderResponse> orderResponses = orders.getContent().stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getUserId(),
                        order.getTotalAmount(),
                        order.getFinalAmount(),
                        order.getVoucherCode(),
                        order.getDiscountAmount(),
                        order.getShippingFee(),
                        (order.getOrderStatus() != null) ? order.getOrderStatus().toString() : "UNKNOWN",
                        order.getOrderDate(),
                        order.getFullName(),
                        order.getEmail(),
                        order.getPhoneNumber(),
                        order.getAddress()
                ))
                .toList();

        return PaginatedResponse.<OrderResponse>builder()
                .totalItems((int)(orders.getTotalElements()))
                .totalPages(orders.getTotalPages())
                .currentPage(orders.getTotalPages())
                .pageSize(orders.getSize())
                .hasNextPage(orders.hasNext())
                .hasPreviousPage(orders.hasPrevious())
                .elements(orderResponses)
                .build();
    }

    public List<OrderItemResponse> getAllItemByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return orderItems.stream().map(orderItem -> {
            Basket basket = orderItem.getBasket(); // Lấy giỏ hàng từ order item
            List<BasketImageResponse> images = basket.getImages().stream()
                    .map(image -> new BasketImageResponse(image.getImageUrl())) // Ánh xạ ảnh của giỏ hàng
                    .toList();

            return new OrderItemResponse(
                    orderItem.getId(),
                    basket.getId(),
                    orderItem.getQuantity(),
                    basket.getDescription(),
                    basket.getName(),
                    basket.getPrice(),
                    images
            );
        }).toList();
    }
        public OrderResponse getOneOrder(Long orderId){
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));
            return new OrderResponse( order.getId(),
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
                    order.getAddress());
        }







}
