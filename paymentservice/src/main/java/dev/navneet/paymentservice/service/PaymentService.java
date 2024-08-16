package dev.navneet.paymentservice.service;

import dev.navneet.paymentservice.service.paymentGateway.PaymentGateway;
import dev.navneet.paymentservice.strategy.PaymentGatewayChooserStrategy;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentGatewayChooserStrategy paymentGatewayChooserStrategy;

    public PaymentService (PaymentGatewayChooserStrategy
                                   paymentGatewayChooserStrategy){
        this.paymentGatewayChooserStrategy = paymentGatewayChooserStrategy;
    }

    public String initiatePayment(String orderId, String email, String phoneNumber, Long amount) {
//        Long amount =10L;
//        return "Hello" + orderId;
        PaymentGateway getPaymentGateway = paymentGatewayChooserStrategy.getIdealPaymentGateway();
        // put the logic of extracting the details from the order and generating a payment link;

        return getPaymentGateway.generatePaymentLink( orderId,  email,  phoneNumber,  amount);
    }
}
