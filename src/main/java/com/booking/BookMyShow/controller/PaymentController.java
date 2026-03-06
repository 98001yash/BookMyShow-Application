package com.booking.BookMyShow.controller;

import com.booking.BookMyShow.dtos.PaymentVerifyRequest;
import com.booking.BookMyShow.service.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/create-order/{bookingReference}")
    public Map<String, Object> createOrder(@PathVariable String bookingReference) throws RazorpayException {
        return paymentService.createPaymentOrder(bookingReference);
    }

    @PostMapping("/verify")
    public String verifyPayment(@RequestBody PaymentVerifyRequest request) {
        paymentService.verifyPayment(request);
        return "Payment verified and booking confirmed";
    }
}