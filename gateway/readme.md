# API Gateway + Load Balancer

The **API Gateway** acts as a central entry point for client requests, routing them to appropriate backend microservices like **User Service** and **Product Service**. The gateway also serves as a **Load Balancer**, distributing incoming requests evenly across multiple instances of the same service. It uses **Spring Cloud Gateway** and **Spring Cloud Load Balancer** with **Eureka Service Discovery** for dynamic routing and balancing.

## Key Features

1. **Routing & Forwarding**:

   - The **API Gateway** routes incoming requests to the appropriate microservice based on the URL pattern.
   - For example, requests starting with `/products` are forwarded to the **Product Service**, while requests starting with `/users` are routed to the **User Service**.

2. **Load Balancing**:

   - The API Gateway acts as a **Load Balancer**, distributing traffic among multiple instances of a service.
   - It uses **client-side load balancing**, which retrieves service instances from **Eureka** and selects one for each request based on a round-robin or other algorithm.

3. **Service Discovery Integration**:

   - The **API Gateway** integrates with **Eureka Service Discovery** to dynamically discover available microservices.
   - When a request arrives, the API Gateway queries Eureka to get the list of available instances and balances the requests across them.

4. **Path-Based Routing**:

   - Based on URL patterns, the gateway forwards requests to different microservices. For instance:
     - Requests to `/products/**` are routed to the **Product Service**.
     - Requests to `/users/**` are routed to the **User Service**.

5. **Dynamic Scaling**:
   - The API Gateway enables dynamic scaling by allowing new service instances to register with Eureka and participate in load balancing automatically.

## Implementation Details

1. **Gateway Configuration**:

   - The **Spring Cloud Gateway** configuration is defined in the **application.properties** file. It maps URL patterns to services, allowing requests to be routed dynamically.

2. **Predicates**:

   - **Predicates** define conditions that must be met for a request to be routed to a particular service. For example:
     ```properties
     spring.cloud.gateway.routes[0].id=product-service
     spring.cloud.gateway.routes[0].uri=lb://PRODUCTSERVICE
     spring.cloud.gateway.routes[0].predicates[0]=Path=/products/**
     ```

3. **Load Balancer**:

   - The API Gateway uses **Spring Cloud Load Balancer** to distribute requests across multiple instances of the target microservice. It leverages Eureka for dynamic service registration and discovery:
     ```properties
     spring.cloud.gateway.routes[1].id=user-service
     spring.cloud.gateway.routes[1].uri=lb://USERSERVICE
     spring.cloud.gateway.routes[1].predicates[0]=Path=/users/**
     ```

4. **Eureka Discovery Client**:

   - The **Eureka Client** enables the API Gateway to register itself with the **Eureka Server** and discover other microservices.

5. **Monitoring and Metrics**:
   - **Spring Boot Actuator** is used for monitoring the health and performance of the API Gateway. Prometheus is used to scrape these metrics, and **Grafana** is used to visualize them.
   - Prometheus fetches data from the **/actuator/prometheus** endpoint for metrics related to the Gateway's health, response times, memory usage, and more.

## API Gateway Flow

1. **Client Request**:

   - A client sends a request to the API Gateway (e.g., `localhost:8080/products/1`).

2. **Routing**:

   - The API Gateway checks the route configuration in **application.properties** and forwards the request to the appropriate microservice based on the URL pattern.

3. **Service Discovery**:

   - The API Gateway uses **Eureka** to discover instances of the target microservice. It fetches the available instances and forwards the request to one of the instances.

4. **Load Balancing**:

   - The Gateway uses **Spring Cloud Load Balancer** to distribute traffic evenly across multiple instances of the target microservice.

5. **Response**:
   - The selected microservice processes the request and returns the response to the API Gateway, which then sends it back to the client.

## Technologies Used

- **Spring Cloud Gateway**: For routing and forwarding requests.
- **Spring Cloud Load Balancer**: For distributing requests across multiple service instances.
- **Spring Cloud Eureka**: For service discovery and dynamic routing.
- **Spring Boot Actuator**: For monitoring and exposing metrics.
- **Prometheus**: For scraping and collecting metrics.
- **Grafana**: For visualizing metrics and performance data.

## Setup Instructions

1. **Add Dependencies**:

   - Add the necessary dependencies in the **pom.xml** file:
     ```xml
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-gateway</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-loadbalancer</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
     </dependency>
     ```

2. **Gateway Configuration**:

   - Set up the routing and load balancing rules in **application.properties** or **application.yml**:
     ```properties
     spring.application.name=api-gateway
     eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
     spring.cloud.gateway.routes[0].id=product-service
     spring.cloud.gateway.routes[0].uri=lb://PRODUCTSERVICE
     spring.cloud.gateway.routes[0].predicates[0]=Path=/products/**
     spring.cloud.gateway.routes[1].id=user-service
     spring.cloud.gateway.routes[1].uri=lb://USERSERVICE
     spring.cloud.gateway.routes[1].predicates[0]=Path=/users/**
     ```

3. **Enable Discovery Client**:

   - In your main class, enable Eureka Client:
     ```java
     @SpringBootApplication
     @EnableEurekaClient
     public class ApiGatewayApplication {
         public static void main(String[] args) {
             SpringApplication.run(ApiGatewayApplication.class, args);
         }
     }
     ```

4. **Run and Test**:
   - Start the **Eureka Server**, **API Gateway**, and other microservices (e.g., **User Service** and **Product Service**).
   - Send requests to the Gateway (e.g., `localhost:8080/products/1`), and observe the Gateway routing and load balancing the requests to the respective services.

## Monitoring with Prometheus and Grafana

1. **Add Actuator and Prometheus Dependencies**:

   - Add the following dependencies to enable metrics scraping by Prometheus:
     ```xml
     <dependency>
         <groupId>io.micrometer</groupId>
         <artifactId>micrometer-registry-prometheus</artifactId>
     </dependency>
     <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-actuator</artifactId>
     </dependency>
     ```

2. **Configure Prometheus**:

   - Set up Prometheus to scrape the **/actuator/prometheus** endpoint of the Gateway for monitoring.

3. **Visualize with Grafana**:
   - Use **Grafana** to visualize metrics collected by Prometheus, such as CPU usage, request count, memory usage, etc.

## Improvements

- **Caching**: Implement caching in the API Gateway to reduce the load on microservices.
- **Security**: Add JWT or OAuth2-based authentication for secure communication between clients and microservices.
- **Rate Limiting**: Implement rate limiting in the API Gateway to prevent abuse by throttling requests based on IP or user.

## References

- [Spring Cloud Gateway Documentation](https://spring.io/projects/spring-cloud-gateway)
- [Spring Cloud Eureka](https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-eureka-server.html)
- [Prometheus Documentation](https://prometheus.io/docs/introduction/overview/)
- [Grafana Documentation](https://grafana.com/docs/)
