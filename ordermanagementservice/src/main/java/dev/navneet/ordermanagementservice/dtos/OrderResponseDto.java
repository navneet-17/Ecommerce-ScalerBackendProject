package dev.navneet.ordermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private Double totalAmount;
    private String deliveryAddress;
    private String status;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private List<OrderItemDto> products;

    @Override
    public String toString() {
        return "Order Details {" +
                "userId = "+ userId +
                "totalAmount = "+ totalAmount +
                "deliveryAddress = "+ deliveryAddress +
                "status = "+ status +
                "orderDate = "+ orderDate +
                "expectedDeliveryDate = "+ expectedDeliveryDate +
                "products = "+ products +
                '}';
    }



}
