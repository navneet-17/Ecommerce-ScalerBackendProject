package dev.navneet.paymentservice.strategy;


import dev.navneet.paymentservice.service.paymentGateway.PaymentGateway;
import dev.navneet.paymentservice.service.paymentGateway.RazorpayPaymentGateway;
import dev.navneet.paymentservice.service.paymentGateway.StripePaymentGateway;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class PaymentGatewayChooserStrategy {
    private final RazorpayPaymentGateway razorpayPaymentGateway ;
    private final StripePaymentGateway stripePaymentGateway;

    PaymentGatewayChooserStrategy( RazorpayPaymentGateway razorpayPaymentGateway,
                                   StripePaymentGateway stripePaymentGateway){
        this.razorpayPaymentGateway = razorpayPaymentGateway;
        this.stripePaymentGateway = stripePaymentGateway;
    }

    public PaymentGateway getIdealPaymentGateway(){
        int random = new Random().nextInt();
        return (random % 2==0)? razorpayPaymentGateway: stripePaymentGateway;
    }
}
