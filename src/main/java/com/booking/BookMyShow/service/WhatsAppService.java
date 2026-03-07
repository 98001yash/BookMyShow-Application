package com.booking.BookMyShow.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WhatsAppService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.number}")
    private String fromNumber;

    public void sendBookingConfirmation(String toNumber, String messageBody) {

        try {

            log.info("Initializing Twilio client...");

            Twilio.init(accountSid, authToken);

            log.info("Sending WhatsApp message to {}", toNumber);

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toNumber),
                    new PhoneNumber(fromNumber),
                    messageBody
            ).create();

            log.info("WhatsApp message sent successfully!");
            log.info("Twilio Message SID: {}", message.getSid());
            log.info("Message Status: {}", message.getStatus());

        } catch (Exception e) {

            log.error("Failed to send WhatsApp message to {}", toNumber);
            log.error("Error: {}", e.getMessage(), e);

        }
    }
}