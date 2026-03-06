package com.booking.BookMyShow.controller;

import com.booking.BookMyShow.service.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}