package com.hanu.isd.service;

import com.hanu.isd.dto.request.CartRequest;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = Cart.builder().user(user).items(new HashSet<>()).build();
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndBasket(cart, basket);

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

    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        Set<CartItemResponse> items = (cart.getItems() == null ? Set.of() : cart.getItems().stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toSet()));


        cartResponse.setItems(items);
        return cartResponse;
    }

    //Lấy tất cả sản phẩm trong giỏ hàng
    public List<CartItemResponse> getCartItemsByCartId(Long cartId) {
        var cartItems = cartItemRepository.findByCartId(cartId);
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .collect(Collectors.toList());
    }
}
