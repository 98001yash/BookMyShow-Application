package com.booking.BookMyShow.utils;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookingReferenceGenerator {

    private static final String PREFIX = "BMS";
    private static final String CHAR_POOL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");


    public static String generate() {

        String datePart = LocalDate.now().format(DATE_FORMAT);
        String randomPart = generateRandomString(5);
        return PREFIX + "-" + datePart + "-" + randomPart;
    }


    private static String generateRandomString(int length) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHAR_POOL.length());
            builder.append(CHAR_POOL.charAt(index));
        }

        return builder.toString();
    }
}