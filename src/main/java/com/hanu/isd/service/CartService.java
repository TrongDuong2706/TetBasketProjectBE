package com.hanu.isd.service;

import com.hanu.isd.dto.request.CartRequest;
import com.hanu.isd.dto.request.UpdateCartItemRequest;
import com.hanu.isd.dto.response.CartResponse;
import com.hanu.isd.dto.response.CartItemResponse;
import com.hanu.isd.entity.*;
import com.hanu.isd.exception.AppException;
import com.hanu.isd.exception.ErrorCode;
import com.hanu.isd.repository.*;
import com.hanu.isd.mapper.CartMapper;
import com.hanu.isd.mapper.CartItemMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    UserRepository userRepository;
    BasketRepository basketRepository;
    CartItemRepository cartItemRepository;
    CartMapper cartMapper;
    CartItemMapper cartItemMapper;

    @Transactional
    public CartResponse addToCart(CartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Basket basket = basketRepository.findById(cartRequest.getBasketId())
                .orElseThrow(() -> new AppException(ErrorCode.BASKET_NOT_EXISTED));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).items(new HashSet<>()).build();
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndBasket(cart, basket);
        int totalRequestedQuantity = cartRequest.getQuantity();
        if (totalRequestedQuantity > basket.getQuantity()) {
            throw new AppException(ErrorCode.GREATER_THAN_QUANTITY);
        }

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + cartRequest.getQuantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .basket(basket)
                    .quantity(cartRequest.getQuantity())
                    .build();
            cart.getItems().add(cartItem);  // 💡 Quan trọng!
            cartItemRepository.save(cartItem);
        }

        return convertToCartResponse(cart);
    }

    @Transactional
    public CartResponse updateCartItemQuantity(UpdateCartItemRequest request) {
        // Tìm user
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm cart của user
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Tìm basket
        Basket basket = basketRepository.findById(request.getBasketId())
                .orElseThrow(() -> new AppException(ErrorCode.BASKET_NOT_EXISTED));

        // Tìm cart item
        CartItem cartItem = cartItemRepository.findByCartAndBasket(cart, basket)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // Tính số lượng mới
        int newQuantity = cartItem.getQuantity() + request.getQuantityChange();

        if (newQuantity <= 0) {
            // Nếu số lượng <= 0 thì xóa item khỏi cart
            cart.getItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            // ✅ Kiểm tra nếu vượt quá số lượng có sẵn
            if (newQuantity > basket.getQuantity()) {
                throw new AppException(ErrorCode.GREATER_THAN_QUANTITY);
            }

            // Cập nhật số lượng mới
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }

        // Trả về response
        return convertToCartResponse(cart);
    }


    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        Set<CartItemResponse> items = (cart.getItems() == null ? Set.of() : cart.getItems().stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toSet()));


        cartResponse.setItems(items);
        return cartResponse;
    }

    //Lấy tất cả sản phẩm trong giỏ hàng
    public List<CartItemResponse> getCartItemsByUserId(String userId) {
        // Lấy cart của user bằng userId
        var cart = cartRepository.findByUserId(userId);

        // Nếu không tìm thấy giỏ hàng, trả về danh sách rỗng
        if (cart == null) {
            return Collections.emptyList();
        }

        // Lấy các item từ cart và chuyển đổi thành CartItemResponse
        var cartItems = cartItemRepository.findByCartId(cart.get().getId());
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CartResponse removeCartItem(String userId, Long basketId) {
        // Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm cart của user
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Tìm basket
        Basket basket = basketRepository.findById(basketId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Tìm cart item
        CartItem cartItem = cartItemRepository.findByCartAndBasket(cart, basket)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));

        // Xóa item khỏi giỏ hàng
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return convertToCartResponse(cart);
    }

    public Integer countTotalItemsInCart(String userId) {
        // Tìm user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Tìm cart của user
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Nếu giỏ hàng rỗng thì trả về 0
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return 0;
        }

        // Tính tổng số lượng từng item
        return cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }


}
