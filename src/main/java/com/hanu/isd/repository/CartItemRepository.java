package com.hanu.isd.repository;

import com.hanu.isd.entity.Basket;
import com.hanu.isd.entity.Cart;
import com.hanu.isd.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndBasket(Cart cart, Basket basket);
    List<CartItem> findByCartId(Long cartId);
    List<CartItem> findByCart(Cart cart);

}
