package com.booking.BookMyShow.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentVerifyRequest {

    private String bookingReference;

    private String orderId;

    private String paymentId;

    private String signature;

    private Long showId;

    private List<String> seatNumbers;

}
