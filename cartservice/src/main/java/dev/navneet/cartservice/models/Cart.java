package dev.navneet.cartservice.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Cart {
    @Id // Unique identifier for MongoDB
    private String id;
    private Long userId;
    private List<CartItem> cartItems;
    private Double totalPrice;

    public Cart(Long userId, List<CartItem> cartItems, Double totalPrice) {
        this.userId = userId;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId=" + userId +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                '}';
    }

}
