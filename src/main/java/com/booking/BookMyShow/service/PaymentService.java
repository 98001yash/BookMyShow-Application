package com.booking.BookMyShow.service;

import com.booking.BookMyShow.dtos.PaymentVerifyRequest;
import com.booking.BookMyShow.entity.Booking;
import com.booking.BookMyShow.enums.BookingStatus;
import com.booking.BookMyShow.repository.BookingRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final RazorpayClient razorpayClient;
    private final BookingRepository bookingRepository;
    private final SeatLockService seatLockService;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;

    @Value("${razorpay.key-secret}")
    private String razorpaySecret;

    public Map<String, Object> createPaymentOrder(String bookingReference) throws RazorpayException {

        Booking booking = bookingRepository
                .findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        int amount = (int) (booking.getTotalAmount() * 100);

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
    public void verifyPayment(PaymentVerifyRequest request, String userEmail) {

        Booking booking = bookingRepository
                .findByBookingReference(request.getBookingReference())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        String payload = request.getOrderId() + "|" + request.getPaymentId();

        String generatedSignature = generateSignature(payload, razorpaySecret);

        if (!generatedSignature.equals(request.getSignature())) {
            throw new RuntimeException("Payment verification failed");
        }

        // Confirm seats
        seatLockService.confirmSeats(
                booking.getShow().getId()
        );

        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);

        // 📩 Send Email
        if (userEmail != null && !userEmail.isBlank()) {

            emailService.sendBookingConfirmation(
                    userEmail,
                    booking.getBookingReference(),
                    booking.getShow().getMovie().getTitle(),
                    booking.getShow().getStartTime().toString()
            );

        } else {
            log.warn("Email not provided for booking {}", booking.getBookingReference());
        }

        String message =
                "🎬 Booking Confirmed!\n\n" +
                        "Booking Ref: " + booking.getBookingReference() + "\n" +
                        "Movie: " + booking.getShow().getMovie().getTitle() + "\n" +
                        "Show Time: " + booking.getShow().getStartTime() + "\n\n" +
                        "Enjoy your movie! 🍿";

        whatsAppService.sendBookingConfirmation(
                "+917091844941",
                message
        );
    }


    private String generateSignature(String data, String secret) {

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey =
                    new SecretKeySpec(secret.getBytes(), "HmacSHA256");

            mac.init(secretKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            StringBuilder hex = new StringBuilder();

            for (byte b : rawHmac) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Signature generation failed");

        }
    }
    }