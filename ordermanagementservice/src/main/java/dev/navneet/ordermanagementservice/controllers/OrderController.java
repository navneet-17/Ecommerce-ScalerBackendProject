package dev.navneet.ordermanagementservice.controllers;

import dev.navneet.ordermanagementservice.dtos.CheckoutRequestDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import dev.navneet.ordermanagementservice.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 1. Create an Order
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CheckoutRequestDto checkoutRequestDto) {
        OrderResponseDto orderResponseDto = orderService.createOrder(checkoutRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    // 2. Get Orders by User ID
    @GetMapping("/{userId}")
    public  ResponseEntity<?>  getOrdersByUserId(@PathVariable Long userId) {
        // Delegate to the service and pass along the ResponseEntity
        return orderService.getOrdersByUserId(userId);
    }

    // 3. Get Specific Order by Order ID
    @GetMapping("/track/{orderId}")
    public ResponseEntity<OrderResponseDto> trackOrder(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // 4. Update Order Status
    @PutMapping("/{orderId}/update-status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // 5. Cancel Order
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }
}
