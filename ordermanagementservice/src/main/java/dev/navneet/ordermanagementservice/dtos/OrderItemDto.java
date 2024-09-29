package dev.navneet.ordermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double totalItemPrice;

    @Override
    public String toString() {
        return "Order Details {" +
                "productId = "+ productId +
                "quantity = "+ quantity +
                "price = "+ price +
                "totalItemPrice = "+ totalItemPrice +
                '}';
    }

}
