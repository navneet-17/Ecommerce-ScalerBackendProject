# Implementing the Payment Service
## Steps of implementation ##
### 1. Create a Payment Controller
     Test the controller mappings via postman. 
### 2. Create a Payment Gateway interface
### 3. Create RazorPay and Stripe Payment Gateway implementation classes of the Payment Gateway interface.
### 4. Create a Payment Service class that will have the logic of implementing the actual payment gateway (called by the implementing classes)
### 5. Create a strategy class for choosing the best current strategy out of the two payment gateways. 
### 6. Call the method of the strategy class to get the best strategy in the Payment Service class.
### 7. Add the RazorPay and Stripe dependency in the project.
### 8.  Get the API key and secret for Razorpay and Stripe and add it to the Env variables and use this env variable in the properties file
### 9. Add the JAVA code to generate a payment link from Razorpay's official doc: https://razorpay.com/docs/api/payments/payment-links/create-standard/
### 10. Create a config file to create a bean of the RazorpayClient object and assign it the secret id and key.  
### 11. Inject the RazorpayClient that we created in the config class Update the generate payment link code as per our requirement
### 12. We pass hard coded values for the time-being for the order details in the generate payment link method. 
### 13. Next, we create the initiate payment request DTO to fetch the order details.
### 14. Update the PaymentController to accept this dto as a request body 
### 15.  Send the request payload dto
### 16. check the link, generation of payment - link is now successful. 
