# Order Service

The **Order Service** is responsible for managing the checkout process, placing orders, tracking order status, and handling order cancellations and updates in an e-commerce system. It interacts with the **Cart Service** to retrieve cart details and supports user-specific order history and order tracking.

## Key Features

1. **Retrieve Cart Details**:

   - Interacts with the **Cart Service** to retrieve cart details for a user before placing an order.

2. **Checkout API**:

   - Provides an API to initiate the checkout process, calculating the total cost and validating payment options.

3. **Place Order**:

   - Processes the order by creating an order entry in the system after a successful checkout.
   - Stores the order details such as items, quantity, total price, delivery address, and payment method.

4. **Order Tracking**:

   - Allows users to track the status of their order (e.g., processing, shipped, delivered).
   - Tracks key milestones in the order lifecycle, such as when the order is placed, processed, or shipped.

5. **Order History**:

   - Provides APIs to view all past orders for a specific user.
   - Allows users to view detailed information about each order they have placed.

6. **Order Cancellation**:

   - Users can cancel an order before it reaches a certain status (e.g., shipped).
   - The service handles the cancellation process and updates the order status accordingly.

7. **Update Order Status**:
   - Updates the status of the order, e.g., from "processing" to "shipped" or "delivered."
   - Can be used by internal services or admin interfaces to manage the order lifecycle.

## API Endpoints

1. **GET /cart/{userId}**:

   - Retrieves cart details from the Cart Service for the specified user.

2. **POST /checkout**:

   - Initiates the checkout process, validates payment, and calculates the total price.

3. **POST /order**:

   - Places an order based on the user's cart, delivery details, and payment information.

4. **GET /orders/{userId}**:

   - Retrieves all orders placed by the user.

5. **GET /order/{orderId}**:

   - Retrieves specific order details for the given order ID.

6. **GET /order/{orderId}/status**:

   - Fetches the current status of the specified order (e.g., shipped, delivered).

7. **DELETE /order/{orderId}**:

   - Cancels the specified order.

8. **PUT /order/{orderId}/status**:
   - Updates the order status to a new value (e.g., shipped, delivered).

## Implementation Details

1. **Create an Order**:

   - The service interacts with the **Cart Service** to retrieve the user's cart details, validates the cart contents, and creates an order.
   - The order details include the product IDs, quantity, total price, delivery address, and payment method.

2. **Track an Order**:

   - Users can track their order status via the **GET /order/{orderId}/status** endpoint.
   - The service tracks the order lifecycle, updating the status as it progresses from placed to delivered.

3. **Get All Orders for a User**:

   - Users can view their entire order history through the **GET /orders/{userId}** endpoint, which retrieves all past orders.

4. **Update Order Status**:

   - Admins or internal services can update the status of an order via the **PUT /order/{orderId}/status** endpoint. The service supports statuses like processing, shipped, delivered, and cancelled.

5. **Cancel an Order**:
   - Orders can be cancelled before they reach certain statuses (e.g., shipped). The service cancels the order and updates its status to reflect this change.

## Technologies Used

- **Spring Boot**: For building RESTful APIs.
- **MySQL**: For persisting order data.
- **MongoDB**: For storing flexible order data structures.
- **Redis**: (Optional) For caching frequently accessed order details.
- **Kafka**: For handling asynchronous communication related to order events.

## Improvements

- **Order Confirmation Email**: Integrate with an email service (e.g., Kafka messaging) to send order confirmation emails.
- **Retry Mechanism**: Implement a retry mechanism for failed order creation attempts due to network or system errors.
- **Order Analytics**: Add support for tracking order metrics (e.g., total sales, top products) for business intelligence.

## References

- [Spring Boot REST APIs](https://spring.io/guides/gs/rest-service/)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
