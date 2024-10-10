# Kafka Email Service

The **Email Service** is a microservice responsible for handling asynchronous email notifications via Kafka messaging. This service consumes messages from Kafka and sends emails to users, such as welcome emails upon user registration.

## Key Features

1. **Asynchronous Communication**:

   - Leverages **Kafka** for asynchronous messaging between the **User Service** and **Email Service**.
   - Ensures decoupling of services, allowing the User Service to trigger email notifications without waiting for them to be processed.

2. **Email Notifications**:

   - The service listens to the **"send-email"** Kafka topic for new user registration events and sends a confirmation or verification email to the user.
   - The emails are sent using **SMTP** through Gmail or other providers.

3. **Producer-Consumer Architecture**:
   - **Producer**: The **User Service** acts as a Kafka producer, sending a message to the Kafka queue after user registration.
   - **Consumer**: The **Email Service** consumes the messages from the Kafka queue and processes them to send emails asynchronously.

## Implementation Details

### Kafka Producer (User Service)

1. **Kafka Configuration**:

   - The **User Service** includes Kafka dependencies and configurations to produce messages on user registration.
   - A Kafka topic **"send-email"** is used to queue email requests.

2. **Producer Code**:
   - Upon successful user registration, a **SendEmailMessageDTO** is created and serialized as a JSON object.
   - This JSON object is then sent to the **"send-email"** topic in Kafka.

### Kafka Consumer (Email Service)

1. **Kafka Consumer Configuration**:

   - The **Email Service** subscribes to the **"send-email"** Kafka topic.
   - A Kafka listener is configured to consume messages asynchronously using the **@KafkaListener** annotation.

2. **Email Sending Logic**:
   - Upon receiving the message, the Email Service processes it and sends an email using **JavaMail** or any SMTP provider.
   - The email content is created based on the information from the **SendEmailMessageDTO**.

### Handling Email Delivery

- Uses **JavaMail** (SMTP) for sending the emails.
- Gmail SMTP is configured for sending actual emails, requiring **App Password** authentication.

## Producer Code Example (User Service)

```java
@Service
public class AuthService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendEmail(UserDTO userDto) {
        SendEmailMessageDTO emailMessage = new SendEmailMessageDTO(userDto.getEmail(), "Welcome", "Thanks for signing up!");
        ObjectMapper objectMapper = new ObjectMapper();
        String message = objectMapper.writeValueAsString(emailMessage);
        kafkaTemplate.send("send-email", message);
    }
}
```
