package com.booking.BookMyShow.service;

import com.booking.BookMyShow.entity.Booking;
import com.booking.BookMyShow.repository.BookingRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;

    public String createPaymentOrder(String bookingReference) throws RazorpayException {

        Booking booking = bookingRepository
                .findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        JSONObject options = new JSONObject();

        options.put("amount", (int)(booking.getTotalAmount() * 100));
        options.put("currency", "INR");
        options.put("receipt", bookingReference);

        Order order = razorpayClient.orders.create(options);

        return order.get("id");
    }
}