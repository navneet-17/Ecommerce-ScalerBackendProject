# E-Commerce Platform

## Overview

To implement this e-commerce platform, a microservices architecture was chosen to ensure scalability, modularity, and fault tolerance. The system comprises multiple independent services, each responsible for a specific domain, enabling seamless integration, efficient scaling, and isolated fault management. This approach aligns with best practices for building modern, cloud-native applications and provides flexibility for independent development and deployment.

## Key Features

Several key features were developed, each powered by different microservices that work together to provide a cohesive and efficient user experience while maintaining separation of concerns.

### Microservices Overview

1. **User Service**: Manages user registration, authentication, and profile management, ensuring secure access and user data privacy.
2. **Product Service**: Handles product catalog management, including product search, categorization, and detailed information retrieval.
3. **Cart Service**: Manages user cart operations such as adding, removing, and updating products in the cart.
4. **Order Management Service**: Facilitates the order processing flow, including order creation, status tracking, and historical order management.
5. **Payment Service**: Integrates with external payment gateways to handle secure payment processing and transaction verification.
6. **Email Service**: Sends notifications and confirmations, such as order receipts and updates, to keep users informed about their transactions.
7. **Service Discovery**: Ensures dynamic registration and discovery of microservices, enabling efficient communication and scaling across the platform.
8. **API Gateway**: Manages incoming API requests, directing them to the appropriate microservices while handling authentication, authorization, and load balancing.

## Microservices Details

### User Service

The User Service is responsible for managing user-related operations such as registration, login, authentication, and authorization.

#### Key Features:
- **User Registration (Sign-Up)**: Securely hashes and stores credentials using BCrypt with salting.
- **User Login**: Authenticates users and issues a JWT for secure, stateless session management.
- **Token-Based Authentication**: Uses JWT to manage user sessions, reducing server-side overhead.
- **OAuth2 Authorization**: Supports third-party logins through OAuth2.
- **Password Security**: Utilizes BCrypt for password hashing with salting.
- **Session Management**: Tracks JWT tokens and session status.
- **Logout**: Ends user sessions by invalidating JWT tokens.
- **Role-Based Access Control (RBAC)**: Manages user permissions based on assigned roles.

#### API Endpoints
- **AuthController**:
    - `POST /auth/signup`: Registers a new user.
    - `POST /auth/login`: Authenticates user and returns a JWT token.
    - `POST /auth/logout`: Invalidates the current session.
    - `POST /auth/validateToken`: Validates a JWT token.
- **RoleController**:
    - `POST /roles`: Assigns a role to a user.
- **UserController**:
    - `GET /users`: Fetches details of all users.
    - `GET /users/{id}`: Fetches user details by user ID.
    - `POST /users/{id}/roles`: Assigns roles to a user.

#### Database Schema
- **Users Table**:
    - `id`, `email`, `password`
- **Roles Table**:
    - `id`, `role`
- **User Roles Table**:
    - `roles_id`, `users_id`
- **Sessions Table**:
    - `id`, `token`, `created_at`, `expiring_at`, `last_logged_in_at`, `logged_out_at`, `user_id`, `status`

### Product Service

The Product Service manages all product-related operations, ensuring efficient handling of product data.

#### Key Features:
- **Product Management**: Allows creation, update, retrieval, and deletion of product entries.
- **Product Retrieval and Search**: Supports pagination and category-based filtering.
- **Category Management**: Retrieves and displays product categories.
- **Search Functionality**: Implements search features for product queries.

#### API Endpoints
- **ProductController**:
    - `POST /products`: Creates a new product entry.
    - `GET /products`: Retrieves a list of all products.
    - `GET /products/{id}`: Fetches details of a specific product.
    - `PUT /products/{id}`: Updates a product by ID.
    - `DELETE /products/{id}`: Deletes a product.
    - `GET /products/categories`: Lists all product categories.
    - `GET /products/categories/{categoryName}`: Retrieves products within a specific category.

#### Database Schema
- **Products Table**:
    - `id`, `currency`, `price`
- **Prices Table**:
    - `id`, `currency`, `price`
- **Categories Table**:
    - `id`, `name`

### Cart Service

The Cart Service manages user shopping carts, providing functionalities like adding items to the cart.

#### Key Features:
- **Add to Cart**: Enables users to add products with custom attributes.
- **View Cart**: Retrieves the contents of the cart.
- **Remove Product from Cart**: Allows removal of specific products.
- **Clear Cart**: Provides functionality to empty the cart.

#### API Endpoints
- **CartController**:
    - `POST /api/cart`: Adds an item to the user's cart.
    - `GET /api/cart/{userId}`: Fetches cart details for the given user ID.
    - `DELETE /api/cart/{userId}`: Clears the cart.
    - `DELETE /api/cart/{userId}/product/{productId}`: Removes a specific product.

### Order Management Service

The Order Management Service manages the checkout process and order management.

#### Key Features:
- **Create Order**: Validates cart and payment details to create an order entry.
- **Track Order**: Allows users to track order statuses.
- **Order History**: Provides APIs to fetch past orders.
- **Order Cancellation**: Enables order cancellations before shipping.
- **Update Order Status**: Manages order lifecycle updates.

#### API Endpoints
- `POST /api/orders/create`: Initiates order creation.
- `GET /api/orders/{userId}`: Retrieves a list of all orders for a user.
- `GET /api/orders/track/{orderId}`: Fetches order details by ID.
- `PUT /api/orders/{orderId}/update-status`: Updates order status.
- `PUT /api/orders/{orderId}/cancel`: Cancels an order.

#### Database Schema
- **Orders Table**:
    - `id`, `order_date`, `status`, `user_id`, `total_amount`, `delivery_address`, `expected_delivery_date`
- **Order Items Table**:
    - `id`, `price`, `product_id`, `quantity`, `order_id`, `total_item_price`

### Payment Service

The Payment Service manages payment operations, integrating with the RazorPay API.

#### Key Features:
- **Payment Link Generation**: Creates secure payment links based on order details.
- **Payment Initiation**: Processes payment requests.
- **Integration with RazorPay**: Uses RazorPay client library for payment management.

#### API Endpoints
- `POST /payments`: Initiates the payment process for a given order.

### Email Service

The Email Service manages email notifications and uses Apache Kafka for message communication.

#### Key Features:
- **Asynchronous Email Processing**: Listens for user events to send emails.
- **Kafka Integration**: Uses Kafka to handle email messages.

### Service Discovery

The Service Discovery microservice manages the registration and discovery of other microservices.

#### Key Features:
- **Service Registration**: Automatically registers microservices upon startup.
- **Service Discovery**: Queries Eureka for available instances.
- **Client-Side Load Balancing**: Uses RestTemplate for inter-service communication.

### API Gateway

The API Gateway serves as the central entry point for client requests and routes them to appropriate microservices.

#### Key Features:
- **Routing & Forwarding**: Routes client requests based on URL patterns.
- **Load Balancing**: Distributes traffic among multiple instances.
- **Service Discovery Integration**: Locates microservices dynamically for routing.


