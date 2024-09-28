package dev.navneet.cartservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CartDto implements Serializable {
    private Long userId;
    private List<CartItemDto> cartItems;
    private Double totalPrice;

    @Override
    public String toString() {
        return "CartDto{" +
                "userId=" + userId +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

