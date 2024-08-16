package dev.navneet.paymentservice.controller;

import dev.navneet.paymentservice.dtos.InitiatePaymentRequestDto;
import dev.navneet.paymentservice.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/")
    public String initiatePayment(@RequestBody InitiatePaymentRequestDto request) {
        return paymentService.initiatePayment(request.getOrderId(), request.getEmail(), request.getPhoneNumber(), request.getAmount());
    }
}
