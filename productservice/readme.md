# Product Service

The **Product Service** is responsible for managing product data, handling search functionality, and implementing pagination and sorting capabilities. Additionally, it supports **Elasticsearch** for optimized product search operations and **Redis** caching to improve response times when retrieving products.

## Key Features

1. **Product Search**:

   - Provides an API to search for products by title and description.
   - Implements both **GET** and **POST** requests for search operations, allowing complex queries and filters with **POST** requests.

2. **Pagination**:

   - Supports paginated retrieval of products.
   - Uses the **PageRequest** class to return paginated results, allowing control over page size and page number.
   - Pagination enhances the performance of product listing by limiting the number of records fetched in each request.

3. **Sorting**:

   - Supports sorting products by multiple criteria, such as price, title, and inventory count.
   - Sorting order (ascending/descending) can be customized for each sorting parameter.
   - A **SortParameter DTO** is used to define the sorting field and order in the search request.

4. **Redis Caching**:
   - Caches frequently accessed products in **Redis** to reduce the load on the database and improve response times.
   - When products are retrieved via the **GET /products** API, they are first checked in Redis. If the data exists, it is served from Redis, reducing the response time significantly.
   - If the product data is not available in Redis, it is fetched from the database, stored in Redis, and then returned to the user.

## API Endpoints

### ProductController

- **POST /search**: Searches products based on the given query and filters.
- **GET /products?page=0&size=10**: Retrieves a paginated list of products, utilizing **Redis** caching to improve performance for frequently accessed products.

## Implementation Details

1. **Search API**:

   - Allows users to search for products by providing a query string.
   - Both **GET** and **POST** requests are supported, but **POST** is recommended for complex queries that involve multiple filters (e.g., price range, brand).

2. **Pagination**:

   - The pagination implementation uses **Pageable** from **Spring Data JPA**.
   - The **PageRequest** class allows specifying the page number and size, which are used in the repository to fetch paginated results.

3. **Sorting**:

   - Sorting can be applied to the search results by passing a list of sorting criteria.
   - Sorting is implemented using the **Sort** class, which allows sorting by multiple fields in both ascending and descending order.
   - The sorting logic can handle tie-breaks by specifying additional sorting criteria.

4. **Redis Caching**:
   - The **RedisTemplate** bean is injected into the service implementation to interact with Redis.
   - Before retrieving products from the database, the service checks whether the data is available in Redis using the following pattern:
     ```java
     redisTemplate.opsForHash().get("PRODUCTS", productId);
     ```
   - If the product data exists in Redis, it is returned directly, significantly reducing the response time.
   - If the product data is not found in Redis, it is fetched from the database, stored in Redis for future access, and then returned to the user.

## Technologies Used

- **Spring Boot**: For building the RESTful APIs.
- **Spring Data JPA**: For repository management and database access.
- **MySQL**: For persistent product data storage.
- **Redis**: For caching product data and reducing database load.
- **Pageable and Sort**: For implementing pagination and sorting functionality in Spring.

## Improvements

- **Redis Expiry**: Implement expiry times for cached product data to ensure that Redis stores the most up-to-date information.
- **ElasticSearch**: Implement Elasticsearch for improving queries and indexing strategies for larger datasets.
- **Faceted Search**: Implement faceted search functionality to allow filtering based on product categories, price ranges, and other attributes.

## References

- [Elasticsearch Spring Boot Integration](https://www.baeldung.com/spring-data-elasticsearch-tutorial)
- [AWS OpenSearch Documentation](https://docs.aws.amazon.com/opensearch-service/latest/developerguide/what-is.html)
- [Spring Data Redis Documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/)
