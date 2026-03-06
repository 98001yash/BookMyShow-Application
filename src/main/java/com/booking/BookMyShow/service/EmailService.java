package com.booking.BookMyShow.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    @Async
    public void sendBookingConfirmation(String toEmail,
                                        String bookingReference,
                                        String movieName,
                                        String showTime){

        if(toEmail == null || toEmail.isBlank()){
            System.out.println("Email is null. Skipping email notification.");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("🎬 Booking Confirmed - BookMyShow");

        message.setText(
                "Your booking is confirmed!\n\n" +
                        "Booking Reference: " + bookingReference + "\n" +
                        "Movie: " + movieName + "\n" +
                        "Show Time: " + showTime + "\n\n" +
                        "Enjoy your movie! 🍿"
        );

        mailSender.send(message);
    }
}
