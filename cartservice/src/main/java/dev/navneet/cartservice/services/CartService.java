package dev.navneet.cartservice.services;

import dev.navneet.cartservice.dtos.CartDto;

public interface CartService {
    CartDto addToCart(CartDto cartDto) ;

    CartDto getCartByUserId(Long userId);

    CartDto clearCart(Long userId);

    CartDto removeProductFromCart(Long userId, Long productId);
}
