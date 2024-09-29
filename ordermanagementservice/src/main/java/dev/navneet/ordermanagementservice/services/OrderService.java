package dev.navneet.ordermanagementservice.services;

import dev.navneet.ordermanagementservice.dtos.CheckoutRequestDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(CheckoutRequestDto checkoutRequestDto);
    ResponseEntity<?> getOrdersByUserId(Long userId);
    OrderResponseDto getOrderById(Long orderId);
    OrderResponseDto updateOrderStatus(Long orderId, String status);
    ResponseEntity<String> cancelOrder(Long orderId);
}
