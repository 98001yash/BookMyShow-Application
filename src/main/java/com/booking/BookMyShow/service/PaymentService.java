package com.booking.BookMyShow.service;

import com.booking.BookMyShow.entity.Booking;
import com.booking.BookMyShow.repository.BookingRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;

    public Map<String, Object> createPaymentOrder(String bookingReference) throws RazorpayException {

        Booking booking = bookingRepository
                .findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        int amount = (int)(booking.getTotalAmount() * 100);

        JSONObject options = new JSONObject();
        options.put("amount", amount);
        options.put("currency", "INR");
        options.put("receipt", bookingReference);

        Order order = razorpayClient.orders.create(options);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.get("id"));
        response.put("amount", amount);

        return response;
    }
}