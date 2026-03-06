package com.booking.BookMyShow.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentVerifyRequest {

    private String paymentReference;
    private String paymentId;
    private String orderId;
    private String signature;
}
