# Cart Service

The **Cart Service** is responsible for managing user carts in an e-commerce system, handling operations such as adding items to the cart, viewing cart items, and emptying the cart. The service is backed by **MongoDB** for flexible data storage and **Redis** for caching to improve performance.

## Key Features

1. **Add to Cart**:

   - Allows users to add items to their cart with custom attributes like color, size, and more.
   - Data is stored in **MongoDB**, allowing flexible data models for additional product attributes such as warranty and discount.

2. **View Cart**:

   - Users can view the contents of their cart.
   - Caching with **Redis** is implemented for faster access to cart data, improving response times for frequently accessed data.

3. **Empty Cart**:
   - Users can empty their cart, removing all items.

## Implementation Details

### MongoDB for Data Storage

- MongoDB is used as the primary database for storing cart data due to its flexibility in handling dynamic fields.
- Attributes like warranty, discount, and delivery options are dynamically stored and retrieved from the database.

### Redis for Caching

- Redis is used to cache cart data, ensuring faster retrieval when the user accesses their cart.
- The cache is updated with each cart modification, and cart data is fetched from Redis when available, reducing the load on MongoDB.

### Dynamic Fields in Payload

- The cart service supports dynamic fields, meaning users can add custom attributes (like warranty or delivery instructions) to their cart items.
- These fields are stored in MongoDB and retrieved dynamically based on the request payload.

## API Endpoints

### CartController

- **POST /cart**: Adds an item to the user's cart.
- **GET /cart/{userId}**: Retrieves the current contents of the user's cart.
- **DELETE /cart/{userId}**: Empties the user's cart.

## Best Practices Implemented

1. **Response Handling**:

   - The service consistently returns responses using best practices for HTTP status codes and detailed response bodies.

2. **Controller Advice**:

   - All exceptions are handled globally using **@ControllerAdvice** to ensure consistent error handling across the application.

3. **DTO Conversion in Service Layer**:
   - DTO (Data Transfer Object) conversion is handled in the service layer, keeping the controllers clean and focused only on handling HTTP requests and responses.

## Caching Flow

1. When a user accesses their cart for the first time, the cart data is retrieved from **MongoDB** and cached in **Redis**.
2. Subsequent requests for the same user's cart fetch the data from **Redis** if available, improving response time.
3. Cache invalidation occurs whenever the cart is modified (item added or cart emptied), ensuring the cache reflects the most recent data.

## Technologies Used

- **Spring Boot**: For building REST APIs.
- **MongoDB**: As the NoSQL database for flexible storage of cart items.
- **Redis**: For caching cart data and improving read performance.
- **Docker**: Used to set up the Redis instance.

## Improvements

- **Cart Item Expiry**: Implement an expiry mechanism for cart items, allowing items to be removed after a specified duration.
- **Advanced Caching**: Add more advanced caching strategies such as lazy loading and cache warming for optimizing performance further.

## References

- [MongoDB Documentation](https://www.mongodb.com/docs/)
- [Redis Documentation](https://redis.io/documentation)
