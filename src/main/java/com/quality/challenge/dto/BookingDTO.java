package com.quality.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateFrom;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateTo;
    @NotEmpty
    private String destination;
    @NotEmpty
    private String hotelCode;
    @Min(1)
    @NotNull
    private Integer peopleAmount;
    @NotEmpty
    private String roomType;
    @NotNull
    @Valid
    private List<PeopleDTO> people;
    @NotNull
    @Valid
    private PaymentMethodDTO paymentMethod;
}
