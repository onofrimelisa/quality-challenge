package com.quality.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class FlightReservationDTO {
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateFrom;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateTo;
    @NotEmpty
    private String destination;
    @NotEmpty
    private String origin;
    @NotEmpty
    private String flightNumber;
    @NotNull
    private Integer seats;
    @NotEmpty
    private String seatType;
    @NotNull
    @Valid
    private List<PeopleDTO> people;
    @NotNull
    @Valid
    private PaymentMethodDTO paymentMethod;
}
