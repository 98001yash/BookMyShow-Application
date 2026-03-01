package com.booking.BookMyShow.dtos.Theatre;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTheatreRequest {

    private String name;
    private String address;
    private String contactNumber;
    private Boolean active;
}
