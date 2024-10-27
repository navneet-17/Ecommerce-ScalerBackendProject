package dev.navneet.ordermanagementservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.navneet.ordermanagementservice.config.KafkaProducerClient;
import dev.navneet.ordermanagementservice.dtos.CheckoutRequestDto;
import dev.navneet.ordermanagementservice.dtos.OrderResponseDto;
import dev.navneet.ordermanagementservice.dtos.SendEmailMessageDto;
import dev.navneet.ordermanagementservice.dtos.UserDto;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final KafkaProducerClient kafkaProducerClient;
    private final ObjectMapper objectMapper;
    private final UserClient userClient;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper, UserClient userClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
        this.userClient = userClient;
    }

    // Create an order based on the checkout request
    @Override
    public OrderResponseDto createOrder(CheckoutRequestDto checkoutRequestDto) {
        Order order = new Order();
        order.setUserId(checkoutRequestDto.getUserId());
        order.setDeliveryAddress(checkoutRequestDto.getDeliveryAddress());
        order.setTotalAmount(checkoutRequestDto.getTotalAmount());
        order.setPaymentMethod(checkoutRequestDto.getPaymentMethod());
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
                .toList();

        order.setOrderItems(orderItems);
       logger.info("\n Order details before payment details: \n {} ", order);

        // Step 3: Save the order with PENDING status
        Order savedOrder = orderRepository.save(order);

        // Step 4: Simulate payment processing (mocked using Thread.sleep)
        String paymentResponse = processMockPayment(savedOrder);

        // Step 5: Update the order status based on payment response
        if (paymentResponse.equalsIgnoreCase("Payment successful"))
            savedOrder.setStatus(OrderStatus.PLACED);
        else
            savedOrder.setStatus(OrderStatus.PENDING_PAYMENT);

        // Send confirmation email to the user for the order placed
        Long userId = checkoutRequestDto.getUserId();
        UserDto userDto = fetchUserDetails(userId);

        // Proceed with email sending if user details are available
        if (userDto != null) {
            try {
                // Sending signup message to Kafka (assuming it's relevant)
                kafkaProducerClient.sendMessage("orderCreated", objectMapper.writeValueAsString(userDto));
                // Preparing conditional email message based on payment status
                SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                emailMessage.setTo(userDto.getEmail());
                emailMessage.setFrom("admin@scaler.com");

                if (savedOrder.getStatus() == OrderStatus.PLACED) {
                    // Payment successful email content
                    emailMessage.setSubject("Order Confirmation for Order ID " + savedOrder.getId());
                    emailMessage.setBody("Dear " + userDto.getEmail() + ",\n\n" +
                            "Thank you for placing an order with us! Your payment was successful, and your order is now being processed. " +
                            "It will be delivered to the specified address shortly.\n\n" +
                            "Order Details:\n" +
                            "Order ID: " + savedOrder.getId() + "\n" +
                            "Total Amount: " + savedOrder.getTotalAmount() + "\n" +
                            "Payment Method: " + savedOrder.getPaymentMethod() + "\n" +
                            "Delivery Address: " + savedOrder.getDeliveryAddress() + "\n" +
                            "Expected Delivery Date: " + savedOrder.getExpectedDeliveryDate() + "\n\n" +
                            "Thank you for choosing Scaler.\n\nBest Regards,\nTeam Scaler");
                }
                else if (savedOrder.getStatus() == OrderStatus.PENDING_PAYMENT) {
                    // Payment unsuccessful email content
                    emailMessage.setSubject("Payment Required for Order ID " + savedOrder.getId());
                    emailMessage.setBody("Dear " + userDto.getEmail() + ",\n\n" +
                            "We noticed an issue with your recent order payment, and it could not be processed successfully. " +
                            "Please retry the payment within the next 10 minutes to complete your order. Your items are being held in the cart.\n\n" +
                            "Order Details:\n" +
                            "Order ID: " + savedOrder.getId() + "\n" +
                            "Order Details: " + savedOrder.getOrderItems() + "\n" +
                            "Total Amount: " + savedOrder.getTotalAmount() + "\n" +
                            "Delivery Address: " + savedOrder.getDeliveryAddress() + "\n" +
                            "Expected Delivery Date: " + savedOrder.getExpectedDeliveryDate() + "\n\n" +
                            "If you have any questions, please contact our support team.\n\nBest Regards,\nTeam Scaler");
                }

                // Sending email message to Kafka
                kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(emailMessage));
                logger.info("Order confirmation email successfully sent for userId: {}", userId);

            } catch (Exception e) {
                // Log error if there's an issue in sending the email or order message
                logger.error("Error occurred while processing order email for userId {}: {}", userId, e.getMessage());
            }
        } else {
            logger.warn("Order confirmation email not sent as user details are unavailable for userId: {}", userId);
        }

            logger.info("\n Saved order details post payment service call : {} \n ", savedOrder);

            // Step 5: Convert to OrderResponseDto and return
            logger.info("\n Returning OrderResponse dto details: {} \n ", savedOrder);
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

        // Return only the non-archived orders
        List<Order> nonArchivedOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.ARCHIVED).toList();

        // If there are no non-archived orders, return an empty list
        if (nonArchivedOrders.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        // Otherwise, return the list of non-archived orders
        List<OrderResponseDto> orderResponseDtos = nonArchivedOrders.stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();

        return ResponseEntity.ok(orderResponseDtos);
    }

    public List<OrderResponseDto> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::toOrderResponseDto).toList();
    }
    // Fetch a specific order by order ID
    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No Order was found with id: "+ orderId ));
        return orderMapper.toOrderResponseDto(order);
    }

    public List<OrderResponseDto> getArchivedOrders(Long userId) {
        // Fetch the archived orders for the user
        Optional<List<Order>> optionalOrders = orderRepository.findByStatusAndUserId(OrderStatus.ARCHIVED, userId);

        // If orders are present, map them to OrderResponseDto; otherwise, return an empty list
        return optionalOrders
                .map(orders -> orders.stream()
                        .map(orderMapper::toOrderResponseDto)
                        .toList())
                .orElse(Collections.emptyList());
    }

    // Update order status (e.g., shipped, delivered, etc.)
    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No Order was found with id: "+ orderId ));
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


    // Cancel an order
    @Override
    public ResponseEntity<String> cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " was not found"));

        // Set the order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);


        // Send confirmation email to the user for the order placed
        Long userId = order.getUserId();
        UserDto userDto = fetchUserDetails(userId);

// Proceed with email sending if user details are available
        if (userDto != null) {
            try {
                // Send a Kafka message for order cancellation if required
                kafkaProducerClient.sendMessage("orderCancelled", objectMapper.writeValueAsString(userDto));

                // Prepare email message for order cancellation
                SendEmailMessageDto emailMessage = new SendEmailMessageDto();
                emailMessage.setTo(userDto.getEmail());
                emailMessage.setFrom("admin@scaler.com");
                emailMessage.setSubject("Order Cancellation for Order ID " + orderId);
                emailMessage.setBody("Dear " + userDto.getEmail() + ",\n\n" +
                        "We wish to inform you that your order with Order ID " + orderId + " has been successfully cancelled. " +
                        "If you have already paid for this order, the refund will be processed to the original payment method within 5-7 business days.\n\n" +
                        "Order Details:\n" +
                        "Order ID: " + orderId + "\n" +
                        "Total Amount: " + order.getTotalAmount() + "\n" +
                        "Payment Method: " + order.getPaymentMethod() + "\n" +
                        "Delivery Address: " + order.getDeliveryAddress() + "\n\n" +
                        "We apologize for any inconvenience caused. Please reach out to our support team if you have any questions.\n\n" +
                        "Best Regards,\nTeam Scaler");

                // Send cancellation email message to Kafka
                kafkaProducerClient.sendMessage("sendEmail", objectMapper.writeValueAsString(emailMessage));
                logger.info("Order cancellation email successfully sent for userId: {}", userId);
            } catch (Exception e) {
                logger.error("Error occurred while sending cancellation email for userId {}: {}", userId, e.getMessage());
            }
        }

        // Return a success message with 200 OK status
        String message = "Order was successfully cancelled. If you already paid for your order, "
                + "you will receive your refund on the payment method that you used for payment.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ResponseEntity<String> archiveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + orderId + " was not found"));

        // Set the order status to ARCHIVED
        order.setStatus(OrderStatus.ARCHIVED);
        orderRepository.save(order);

        logger.info("Order with id {} was archived successfully", orderId);

        // Return a success message with 200 OK status
        String message = " Your Order is archived now. You can view it from the archived orders section.";
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

    private UserDto fetchUserDetails(Long userId) {
        UserDto userDto = null;
        try {
            // Fetching user Details
            userDto = userClient.getUserById(userId);
            logger.info("Successfully fetched user details for userId: {}", userId);
        } catch (Exception e) {
            // Log error when user details cannot be fetched
            logger.error("Failed to fetch user details for userId {}: {}", userId, e.getMessage());
        }
        return userDto;
    }
}
