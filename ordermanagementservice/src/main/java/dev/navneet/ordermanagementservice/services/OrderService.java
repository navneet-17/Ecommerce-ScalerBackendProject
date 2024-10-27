package dev.navneet.ordermanagementservice.services;

import dev.navneet.ordermanagementservice.dtos.CheckoutRequestDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    OrderResponseDto createOrder(CheckoutRequestDto checkoutRequestDto);
    ResponseEntity<?> getOrdersByUserId(Long userId);
    OrderResponseDto getOrderById(Long orderId);
    OrderResponseDto updateOrderStatus(Long orderId, String status);
    ResponseEntity<String> cancelOrder(Long orderId);

    ResponseEntity<String> archiveOrder(Long orderId);

    List<OrderResponseDto> getArchivedOrders(Long userId);

    List<OrderResponseDto> getAllOrders();
}
