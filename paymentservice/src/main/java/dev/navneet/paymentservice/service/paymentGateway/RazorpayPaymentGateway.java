package dev.navneet.paymentservice.service.paymentGateway;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

@Service
public class RazorpayPaymentGateway implements PaymentGateway{
    private final RazorpayClient razorpayClient;

    RazorpayPaymentGateway(RazorpayClient razorpayClient){
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String generatePaymentLink(
            String orderId, String email, String phoneNumber, Long amount){
        try{
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",amount * 100); // decimal number conversion logic
            paymentLinkRequest.put("currency","INR");
            paymentLinkRequest.put("accept_partial",false);
            paymentLinkRequest.put("expire_by",1723974147);
            paymentLinkRequest.put("reference_id",orderId);
            paymentLinkRequest.put("description","Payment for order #"+orderId);
            JSONObject customer = new JSONObject();
            customer.put("name","Navneet Shree");
            customer.put("contact", phoneNumber);
            customer.put("email", email);
            paymentLinkRequest.put("customer",customer);
            JSONObject notify = new JSONObject();
            notify.put("sms",true);
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);
            paymentLinkRequest.put("reminder_enable",true);
            JSONObject notes = new JSONObject();
            notes.put("policy_name","Testing RazorPay");
            paymentLinkRequest.put("notes",notes);
            paymentLinkRequest.put("callback_url","https://example-callback-url.com/");
            paymentLinkRequest.put("callback_method","get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            return payment.get("short_url").toString();
        } catch (Exception e) {
            System.out.println(e.toString());
            return "something is wrong";
       }
    }
}
