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
public class FlightReservationRequestDTO {
    @Email
    @NotNull
    private String username;
    @NotNull
    @Valid
    private FlightReservationDTO flightReservation;
}
