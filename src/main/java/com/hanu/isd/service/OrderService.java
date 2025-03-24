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
        log.info("Bắt đầu xử lý đơn hàng cho userId: {}", request.getUserId());

        // ✅ Lấy giỏ hàng của user
        Cart cart = cartRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // ✅ Nhận totalAmount từ FE
        Double totalAmount = request.getTotalAmount();

        // ✅ Tạo Order
        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(totalAmount)
                .finalAmount(totalAmount) // Không cần tính toán lại
                .voucherCode(request.getVoucherCode())
                .discountAmount(0.0) // FE đã xử lý, không cần tính lại
                .shippingFee(0.0) // Nếu có phí ship, FE đã cộng vào rồi
                .orderStatus(OrderStatus.PENDING)
                .orderDate(new Date())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .note(request.getNote())
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
        cartItemRepository.flush();

        // ✅ Xóa cart
        cart.getItems().clear();
        cartRepository.save(cart);
        cartRepository.delete(cart);

        // ✅ Gửi email xác nhận
        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .to(List.of(
                        Recipient.builder().name(request.getFullName()).email(request.getEmail()).build(),
                        Recipient.builder().name("Chủ shop").email("trongdeptrai57@gmail.com").build()
                ))
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
                order.getAddress(),
                order.getNote()
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
                        order.getAddress(),
                        order.getNote()
                ))
                .toList();

        return PaginatedResponse.<OrderResponse>builder()
                .totalItems((int)(orders.getTotalElements()))
                .totalPages(orders.getTotalPages())
                .currentPage(orders.getNumber())
                .pageSize(orders.getSize())
                .hasNextPage(orders.hasNext())
                .hasPreviousPage(orders.hasPrevious())
                .elements(orderResponses)
                .build();
    }

    public PaginatedResponse<OrderResponse> getAllOrderByUserId(int page, int size, String userId
                                                        ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orders = orderRepository.findByUserId(userId, pageRequest);

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
                        order.getAddress(),
                        order.getNote()
                ))
                .toList();

        return PaginatedResponse.<OrderResponse>builder()
                .totalItems((int)(orders.getTotalElements()))
                .totalPages(orders.getTotalPages())
                .currentPage(orders.getNumber())
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
                    order.getAddress(),
                    order.getNote());

        }
        //Change status

    @Transactional
    public OrderResponse changeOrderStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTED));

        OrderStatus currentStatus = order.getOrderStatus();
        OrderStatus newStatus;

        switch (currentStatus) {
            case PENDING -> newStatus = OrderStatus.CONFIRMED;
            case CONFIRMED -> newStatus = OrderStatus.SHIPPED;
            case SHIPPED -> newStatus = OrderStatus.DELIVERED;
            default -> {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        // ✅ Gửi email thông báo trạng thái mới
        String statusMessage = switch (newStatus) {
            case CONFIRMED -> "Đơn hàng của bạn đã được xác nhận.";
            case SHIPPED -> "Đơn hàng của bạn đã được gửi đi.";
            case DELIVERED -> "Đơn hàng của bạn đã được giao thành công.";
            default -> "Đơn hàng của bạn đã thay đổi trạng thái.";
        };

        String htmlContent = String.format("""
        <h2>Thông báo cập nhật đơn hàng</h2>
        <p>Xin chào %s,</p>
        <p>%s</p>
        <p>Mã đơn hàng: #%d</p>
        <p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>
    """, order.getFullName(), statusMessage, order.getId());

        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .to(List.of(Recipient.builder().name(order.getFullName()).email(order.getEmail()).build()))
                .subject("Cập nhật trạng thái đơn hàng")
                .htmlContent(htmlContent)
                .build();

        emailService.sendEmail(emailRequest);

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
                order.getAddress(),
                order.getNote()
        );
    }










}
