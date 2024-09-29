package dev.navneet.ordermanagementservice.services;

import dev.navneet.ordermanagementservice.dtos.CheckoutRequestDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import dev.navneet.ordermanagementservice.exceptions.OrderNotFoundException;
import dev.navneet.ordermanagementservice.models.Order;
import dev.navneet.ordermanagementservice.models.OrderItem;
import dev.navneet.ordermanagementservice.models.OrderStatus;
import dev.navneet.ordermanagementservice.mappers.OrderMapper;
import dev.navneet.ordermanagementservice.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    // Create an order based on the checkout request
    @Override
    public OrderResponseDto createOrder(CheckoutRequestDto checkoutRequestDto) {
        Order order = new Order();
        order.setUserId(checkoutRequestDto.getUserId());
        order.setDeliveryAddress(checkoutRequestDto.getDeliveryAddress());
        order.setTotalAmount(checkoutRequestDto.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);  // Initial status is PENDING
        order.setOrderDate(LocalDateTime.now());
        order.setExpectedDeliveryDate(calculateExpectedDeliveryDate());

        // Map and calculate totalItemPrice for each order item
        List<OrderItem> orderItems = checkoutRequestDto.getProducts().stream()
                .map(itemDto -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemDto.getProductId());
                    orderItem.setPrice(itemDto.getPrice());
                    orderItem.setQuantity(itemDto.getQuantity());

                    // Calculate and set total item price
                    orderItem.setTotalItemPrice(orderItem.getPrice() * orderItem.getQuantity());

                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        System.out.println("\n Order details before payment details: \n "+ order.toString());

        // Step 3: Save the order with PENDING status
        Order savedOrder = orderRepository.save(order);

        // Step 4: Simulate payment processing (mocked using Thread.sleep)
        processMockPayment(savedOrder);

        System.out.println("\n Saved order details post payment service call : \n "+ savedOrder.toString());
        // Step 5: Convert to OrderResponseDto and return
        System.out.println("\n Returning OrderResponse dto details: \n "+ savedOrder.toString());
        return orderMapper.toOrderResponseDto(savedOrder);
    }

    // Fetch all orders for a specific user
    @Override
    public ResponseEntity<?>getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            // Return a custom message if no orders are found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No orders found. Please place an order.");
        }

        // Otherwise, return the list of orders
        List<OrderResponseDto> orderResponseDtos = orders.stream()
                .map(orderMapper::toOrderResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderResponseDtos);
    }

    // Fetch a specific order by order ID
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: "+ orderId +" was not found"));
        return orderMapper.toOrderResponseDto(order);
    }

    // Update order status (e.g., shipped, delivered, etc.)
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: "+ orderId +" was not found"));
        // Validate if the status can be updated manually
        if (!isValidStatusForManualUpdate(status)) {
            throw new IllegalArgumentException("Invalid status update. Only SHIPPED, DELIVERED, or RETURNED can be manually updated.");
        }

        // Update the status of the order
        order.setStatus(OrderStatus.valueOf(status));
        Order updatedOrder = orderRepository.save(order);

        // Return the updated order details
        return orderMapper.toOrderResponseDto(updatedOrder);
    }
//        Order updatedOrder = orderRepository.save(order);
//        return orderMapper.toOrderResponseDto(updatedOrder);
//    }

    // Cancel an order
    @Override
    public ResponseEntity<String> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " was not found"));

        // Set the order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // Return a success message with 200 OK status
        String message = "Order was successfully Cancelled. If you already paid for your order, "
                + "you will receive your refund on your payment method that you used for payment.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    // Utility method to calculate expected delivery date
    private LocalDateTime calculateExpectedDeliveryDate() {
        return LocalDateTime.now().plusDays(7);  // Assume delivery in 7 days
    }

    /* Mock payment processing (simulated)
    Later, if we integrate a real payment gateway,
    we can replace this mock processing with actual communication with the Payment Service or payment gateway,
    while keeping the structure the same.
    * */
    private String processMockPayment(Order order) {
        try {
            logger.info("Processing payment for order ID: {} with amount: {}", order.getId(), order.getTotalAmount());

            // Simulate a 3-second delay to mock the payment process
            TimeUnit.SECONDS.sleep(3);

            // Mocking successful or unsuccessful payment randomly
            boolean isPaymentSuccessful = Math.random() > 0.1;

            if (isPaymentSuccessful) {
                // If payment is successful, set order status to 'PLACED'
                order.setStatus(OrderStatus.PLACED);
                orderRepository.save(order);  // Save the updated order status
                logger.info("Payment successful for order ID: {}, Order status updated to PLACED", order.getId());

                return "Payment successful";
            } else {
                // If payment fails, set order status to 'PENDING-PAYMENT'
                order.setStatus(OrderStatus.PENDING_PAYMENT);
                orderRepository.save(order);  // Save the updated order status
                logger.info("Payment failed for order ID: {}, Order status updated to PENDING-PAYMENT", order.getId());

                return "Payment unsuccessful";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Payment process interrupted for order ID: {}", order.getId());
            throw new RuntimeException("Payment processing failed");
        }
    }

    private boolean isValidStatusForManualUpdate(String status) {
        return status.equals(OrderStatus.SHIPPED.name()) ||
                status.equals(OrderStatus.DELIVERED.name()) ||
                status.equals(OrderStatus.RETURNED.name());
    }
}
