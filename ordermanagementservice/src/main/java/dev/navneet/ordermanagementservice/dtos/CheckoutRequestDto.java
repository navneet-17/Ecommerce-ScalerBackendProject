package dev.navneet.ordermanagementservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequestDto {
    private Long userId;
    private String deliveryAddress;
    private String paymentMethod;
    private Double totalAmount;
    private List<OrderItemDto> products;
}
