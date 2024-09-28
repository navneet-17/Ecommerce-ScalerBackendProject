package dev.navneet.cartservice.mappers;

import dev.navneet.cartservice.dtos.CartDto;
import dev.navneet.cartservice.dtos.CartItemDto;
import dev.navneet.cartservice.models.Cart;
import dev.navneet.cartservice.models.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    // Conversion from Cart to CartDto
    public CartDto toCartDto(Cart cart){
        CartDto cartDto = new CartDto();
        cartDto.setUserId(cart.getUserId());
        cartDto.setCartItems(
                cart.getCartItems().stream()
                        .map(this::toCartItemDto)
                        .collect(Collectors.toList())
        );
        cartDto.setTotalPrice(cart.getTotalPrice());
        return cartDto;
    }

    // Conversion from CartItem to CartItemDto
    public CartItemDto toCartItemDto(CartItem cartItem){
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductId(cartItem.getProductId());
        cartItemDto.setTitle(cartItem.getTitle());
        cartItemDto.setPrice(cartItem.getPrice());
        cartItemDto.setQuantity(cartItem.getQuantity());

        // Set total price for individual CartItem
        cartItemDto.setTotalItemPrice(cartItem.getPrice() * cartItem.getQuantity());

        // Map dynamic fields
        cartItemDto.setAdditionalAttributes(cartItem.getAdditionalAttributes());
        return cartItemDto;
    }

    // Conversion from CartDto to Cart
    public Cart toCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setUserId(cartDto.getUserId());
        cart.setCartItems(
                cartDto.getCartItems().stream()
                        .map(this::toCartItem)
                        .collect(Collectors.toList())
        );
        cart.setTotalPrice(cartDto.getTotalPrice());
        return cart;
    }

    // Conversion from CartItemDto to CartItem
    public CartItem toCartItem(CartItemDto cartItemDto) {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(cartItemDto.getProductId());
        cartItem.setTitle(cartItemDto.getTitle());
        cartItem.setPrice(cartItemDto.getPrice());
        cartItem.setQuantity(cartItemDto.getQuantity());

        // Set total price for individual CartItem
        cartItem.setTotalItemPrice(cartItemDto.getPrice() * cartItemDto.getQuantity());

        // Map dynamic fields
        cartItem.setAdditionalAttributes(cartItemDto.getAdditionalAttributes());

        return cartItem;
    }
}
