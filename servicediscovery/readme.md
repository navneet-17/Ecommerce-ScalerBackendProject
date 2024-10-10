# Service Discovery

The **Service Discovery** is responsible for dynamically registering and discovering microservices in a distributed system. It allows microservices to locate and communicate with each other without needing hardcoded URLs, thus enabling more flexible, scalable, and fault-tolerant architectures. This service uses **Spring Cloud Eureka** for its implementation.

## Key Features

1. **Service Registration**:

   - Microservices (like **User Service**, **Product Service**, and **Payment Service**) automatically register themselves with the **Eureka Server** when they start.
   - Eureka keeps track of all registered instances and provides the necessary details when one service needs to communicate with another.

2. **Service Discovery**:

   - When a microservice (e.g., **Product Service**) needs to call another microservice (e.g., **User Service**), it can query Eureka to get the list of all available instances of the target service.
   - **Client-side load balancing** is implemented to distribute the load among different instances of the microservices.

3. **Fault Tolerance**:

   - If an instance of a microservice goes down, it is automatically deregistered from the Eureka Server.
   - New instances are automatically registered when they come up, allowing the system to scale up or down dynamically.

4. **Client-Side Load Balancing**:
   - When a service queries Eureka for the instances of another service, it receives a list of available instances.
   - The calling service uses **RestTemplate** with **@LoadBalanced** to distribute the load across multiple instances of the target service.

## API Gateway vs Load Balancer

1. **API Gateway**:
   - The **API Gateway** serves as the entry point for client requests. It forwards these requests to the appropriate microservice based on routing logic.
2. **Load Balancer**:
   - The **Load Balancer** ensures that requests are distributed evenly across all available instances of a microservice. This helps prevent any single instance from becoming a bottleneck.

## Technologies Used

- **Spring Boot**: For building microservices.
- **Spring Cloud Eureka**: For implementing service discovery.
- **RestTemplate**: For inter-service communication.
- **@LoadBalanced**: For enabling client-side load balancing.

## Setup Instructions

1. **Setting Up the Eureka Server**:

   - Add the following dependencies to the **pom.xml**:

     ```xml
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
     </dependency>
     ```

   - Enable Eureka in the application by adding the **@EnableEurekaServer** annotation to your main class:

     ```java
     @SpringBootApplication
     @EnableEurekaServer
     public class EurekaServerApplication {
         public static void main(String[] args) {
             SpringApplication.run(EurekaServerApplication.class, args);
         }
     }
     ```

   - Configure **application.yml** for the Eureka Server:

     ```yaml
     server:
       port: 8761

     eureka:
       client:
         register-with-eureka: false
         fetch-registry: false
     ```

2. **Setting Up Eureka Clients (Microservices)**:

   - Add the Eureka client dependency to each microservice's **pom.xml**:

     ```xml
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     </dependency>
     ```

   - Enable Eureka in the microservice by adding the **@EnableEurekaClient** annotation to the main class:

     ```java
     @SpringBootApplication
     @EnableEurekaClient
     public class ProductServiceApplication {
         public static void main(String[] args) {
             SpringApplication.run(ProductServiceApplication.class, args);
         }
     }
     ```

   - Configure **application.yml** for the Eureka Client:
     ```yaml
     eureka:
       client:
         service-url:
           defaultZone: http://localhost:8761/eureka/
     ```

3. **Using RestTemplate with @LoadBalanced**:

   - Define a **RestTemplate** bean in the configuration class:

     ```java
     @Configuration
     public class RestTemplateConfig {

         @Bean
         @LoadBalanced
         public RestTemplate restTemplate() {
             return new RestTemplate();
         }
     }
     ```

   - Now, use the **RestTemplate** to communicate between services:

     ```java
     @Autowired
     private RestTemplate restTemplate;

     public UserDTO getUserById(String userId) {
         String url = "http://user-service/users/" + userId;
         return restTemplate.getForObject(url, UserDTO.class);
     }
     ```

## Improvements

- **Caching with Redis**: To enhance performance, consider caching Eureka service discovery results with Redis.
- **Security**: Add OAuth2 or JWT-based authentication for secure inter-service communication through Eureka.

## References

- [Spring Cloud Netflix Eureka](https://spring.io/guides/gs/service-registration-and-discovery/)
- [Spring Cloud Documentation](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html)
