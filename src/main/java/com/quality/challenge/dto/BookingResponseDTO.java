package com.quality.challenge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class BookingResponseDTO extends ResponseDTO{
    private @Email String username;
    private Double amount;
    private Double interest;
    private Double total;
    private BookingDTO booking;
}
