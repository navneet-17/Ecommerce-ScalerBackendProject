# Payment Service

The **Payment Service** is responsible for handling payment processing, including integration with payment gateways like Razorpay and Stripe, and managing order payments, callbacks, and webhooks. The service ensures secure payment transactions and provides mechanisms for handling payment success, failure, and reconciliation.

## Key Features

1. **Initiate Payment**:

   - Provides APIs to initiate the payment process by generating payment links.
   - Integrates with third-party payment gateways like **Razorpay** and **Stripe** to create payment links for different payment methods (e.g., credit cards, UPI, wallets).

2. **Callback URL**:

   - Handles callback URLs for successful and failed payments.
   - Once payment is complete, the user is redirected to a callback URL that informs the system of the payment status.

3. **Webhooks**:

   - Implements **webhook** support for receiving real-time payment status updates from payment gateways.
   - Ensures the backend is notified of payment status changes even if the callback URL fails due to client-side issues like network failure or browser crashes.

4. **Multiple Payment Gateways**:

   - Uses the **Adapter Design Pattern** to integrate with different payment gateways.
   - Supports multiple payment methods (credit/debit cards, UPI, wallets, etc.) and allows switching between payment providers dynamically using the **Strategy Design Pattern**.

5. **Payment Status and Reconciliation**:
   - Stores and tracks payment statuses in the database.
   - Implements reconciliation processes via **Cron Jobs** or **webhooks** to ensure that no transactions are missed.

## API Endpoints

1. **POST /initiate-payment**:

   - Initiates a payment by generating a payment link using the selected payment gateway.

2. **POST /webhook**:

   - Receives payment status updates directly from the payment gateway via webhooks.

3. **POST /callback-url/success**:

   - Handles successful payment callback notifications from the frontend after the payment process.

4. **POST /callback-url/failure**:
   - Handles failed payment callback notifications from the frontend.

## Implementation Details

1. **Payment Gateways**:
   - Integrates with multiple payment gateways like **Razorpay** and **Stripe**.
   - The **Strategy Design Pattern** is used to dynamically choose the payment gateway based on specific rules (e.g., even/odd random number generation).
2. **Webhooks**:

   - **Webhooks** are implemented to handle asynchronous notifications from payment gateways, ensuring that payment status is updated in real-time even if the client fails to load the callback URL.
   - Webhooks are considered more reliable than callback URLs and are used to prevent potential payment status discrepancies.

3. **Reconciliation**:
   - Implements reconciliation to ensure that no payments are missed.
   - **Cron Jobs** or periodic **webhooks** are used to fetch all transactions and compare them with the database records to identify any discrepancies.

## Payment Flow

1. **Initiate Payment**:

   - The frontend sends a payment request to the Payment Service, which contacts the payment gateway (Razorpay/Stripe) to generate a payment link.
   - The payment link is sent back to the frontend for the user to complete the payment.

2. **Payment Completion**:

   - The user completes the payment on the payment gateway's hosted page (e.g., Razorpay or Stripe).
   - The payment gateway sends a response via the **callback URL** and/or **webhook** to inform the Payment Service of the payment status.

3. **Reconciliation**:
   - If there is any discrepancy (e.g., payment recorded in the payment gateway but missing in the Payment Service database), reconciliation is performed using either **Cron Jobs** or **webhooks** to ensure accurate records.

## Technologies Used

- **Spring Boot**: For building the REST APIs.
- **Razorpay API**: For integrating with Razorpay payment gateway.
- **Stripe API**: For integrating with Stripe payment gateway.
- **Webhooks**: For real-time payment status updates.
- **Cron Jobs**: For periodic reconciliation.

## Improvements

- **Retry Mechanism**: Implement a retry mechanism for webhook failures to handle scenarios where the webhook fails to notify the service.
- **Advanced Gateway Selection**: Enhance the payment gateway selection logic to include factors like transaction type, location, or user preferences.
- **Idempotency**: Add idempotency checks to prevent duplicate payments for the same order by validating order IDs and payment statuses.

## References

- [Razorpay Documentation](https://razorpay.com/docs/)
- [Stripe Documentation](https://stripe.com/docs)
- [Spring Boot](https://spring.io/guides/gs/rest-service/)
