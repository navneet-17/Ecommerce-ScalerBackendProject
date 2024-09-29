package dev.navneet.ordermanagementservice.mappers;

import dev.navneet.ordermanagementservice.dtos.OrderItemDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import dev.navneet.ordermanagementservice.models.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponseDto toOrderResponseDto(Order order) {
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setUserId(order.getUserId());
        orderResponseDto.setTotalAmount(order.getTotalAmount());
        orderResponseDto.setDeliveryAddress(order.getDeliveryAddress());
        orderResponseDto.setStatus(order.getStatus().name());
        orderResponseDto.setOrderDate(order.getOrderDate());
        orderResponseDto.setExpectedDeliveryDate(order.getExpectedDeliveryDate());
        orderResponseDto.setProducts(order.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(orderItem.getProductId());
                    itemDto.setQuantity(orderItem.getQuantity());
                    itemDto.setPrice(orderItem.getPrice());
                    itemDto.setTotalItemPrice(orderItem.getTotalItemPrice());
                    return itemDto;
                }).collect(Collectors.toList()));
        return orderResponseDto;
    }
}
