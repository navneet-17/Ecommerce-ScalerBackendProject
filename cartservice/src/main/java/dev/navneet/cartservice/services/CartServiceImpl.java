package dev.navneet.cartservice.services;

import dev.navneet.cartservice.dtos.CartDto;
import dev.navneet.cartservice.mappers.CartMapper;
import dev.navneet.cartservice.models.Cart;
import dev.navneet.cartservice.models.CartItem;
import dev.navneet.cartservice.repositories.CartRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    // Add a new cart for a user, else add to an existing cart
    @Override
    @CachePut(value = "cart", key = "#cartDto.userId")
    public CartDto addToCart(CartDto cartDto) {

        // Check if user already has a cart
        Optional<Cart> existingCartOpt = cartRepository.findByUserId(cartDto.getUserId());

        Cart cart;
        if (existingCartOpt.isPresent()) {
            // Fetch the existing cart and preserve its _id
            cart = existingCartOpt.get();

            // Convert CartItemDto to CartItem and add them to the existing cart
            List<CartItem> cartItems = cartDto.getCartItems().stream()
                    .map(cartMapper::toCartItem)
                    .toList();

            // updated logic for avoiding product duplication here
            for (CartItem newItem : cartItems) {
                // **Check if the product already exists**
                Optional<CartItem> existingItem = cart.getCartItems().stream()
                        .filter(item -> item.getProductId().equals(newItem.getProductId()))
                        .findFirst();

                if (existingItem.isPresent()) {
                    // **If the item exists, update the quantity**
                    CartItem existingCartItem = existingItem.get();
                    existingCartItem.setQuantity(existingCartItem.getQuantity() + newItem.getQuantity());
                    // Calculate individual item total price
                    existingCartItem.setTotalItemPrice(existingCartItem.getPrice() * existingCartItem.getQuantity());
                } else {
                    // **Otherwise, add the new item to the cart**
                    cart.getCartItems().add(newItem);
                }
            }

            // Preserve the _id field for the cart when saving; this ensures doc is not duplicated for the same user.
            cart.setId(existingCartOpt.get().getId());

        }
        else {
            // Create a new cart
            cart = cartMapper.toCart(cartDto);
        }

        // Calculate and set the total price dynamically for the entire cart.
        double totalPrice = calculateTotalPrice(cart);
        cart.setTotalPrice(totalPrice);


        // Log the CartDto before saving
        cartDto.setTotalPrice(totalPrice);

        // Save the cart (either newly created or updated)
        Cart savedCart = cartRepository.save(cart);

        System.out.println("\n saved Cart: ");
        System.out.println(savedCart);  // After saving the cart, print to ensure attributes are stored.

        // Return the updated or newly created CartDto
        return cartMapper.toCartDto(savedCart);
    }

    // Fetch cart by user ID
    @Override
    @Cacheable(value = "cart", key = "#userId")
    public CartDto getCartByUserId(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        if (optionalCart.isEmpty()) {
            return new CartDto(userId, Collections.emptyList(), 0.0); // Return an empty cart with zero totalPrice
        }

        Cart cart = optionalCart.get();

        // Calculate and set the total price dynamically
        cart.setTotalPrice(calculateTotalPrice(cart));

        return cartMapper.toCartDto(cart);
    }


    // Clear the entire cart for a user and return an empty cart
    @Override
    @CacheEvict(value = "cart", key = "#userId")
    public CartDto clearCart(Long userId) {
        System.out.println("Cart was cleared. Add items to your cart for checkout!");

        // Find the cart and delete it if present
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        cartOpt.ifPresent(cartRepository::delete);

        // Return an empty cart DTO (or null if no cart exists)
        return new CartDto(userId, new ArrayList<>(), 0.0);
    }

    // Remove a specific product from the cart
    @Override
    public CartDto removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> new Cart(userId, new ArrayList<>(), 0.0));

        // Find the product in the cart
        Optional<CartItem> cartItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();

            // Decrease the quantity by 1
            cartItem.setQuantity(cartItem.getQuantity() - 1);

            // If quantity reaches 0, remove the product from the cart
            if (cartItem.getQuantity() <= 0) {
                cart.getCartItems().remove(cartItem);
                System.out.println( cartItem.getTitle() +" was removed from the cart.");
            } else {
                System.out.println( "Decreased "+ cartItem.getTitle() +"'s quantity by 1 from the cart.");
                // Otherwise, recalculate the totalItemPrice for the product
                cartItem.setTotalItemPrice(cartItem.getPrice() * cartItem.getQuantity());
            }
        } else {
            // If the product is not found, log it and return the current cart
            System.out.println("Product with ID " + productId + " not found in user's cart.");
            return cartMapper.toCartDto(cart);
        }

        // Calculate and set the total price dynamically for the entire cart
        cart.setTotalPrice(calculateTotalPrice(cart));

        // Save updated cart and return it
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartDto(updatedCart);
    }

    private double calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

}
