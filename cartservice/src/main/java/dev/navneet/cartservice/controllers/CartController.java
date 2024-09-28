package dev.navneet.cartservice.controllers;

import dev.navneet.cartservice.dtos.CartDto;
import dev.navneet.cartservice.mappers.CartMapper;
import dev.navneet.cartservice.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    public CartController(CartService cartService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
    }

    @PostMapping
    public ResponseEntity<CartDto> addToCart(@RequestBody CartDto cartDto) {
        CartDto savedCart = cartService.addToCart(cartDto);
        return ResponseEntity.ok((savedCart));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @DeleteMapping("/{userId}")
    public CartDto clearCart(@PathVariable Long userId) {
        return cartService.clearCart(userId);
    }

    @DeleteMapping("/{userId}/product/{productId}")
    public CartDto removeProductFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return cartService.removeProductFromCart(userId, productId);
    }
}
