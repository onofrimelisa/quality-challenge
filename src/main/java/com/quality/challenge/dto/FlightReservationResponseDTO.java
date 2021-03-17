package com.quality.challenge.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class FlightReservationResponseDTO extends ResponseDTO {
    @Email
    @NotNull
    private String username;
    @NotNull
    private Double amount;
    @NotNull
    private Double interest;
    @NotNull
    private Double total;
    @Valid
    @NotNull
    private FlightReservationDTO flightReservation;
}
