package com.quality.challenge.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {
    @NotEmpty
    private String type;
    @NotEmpty
    private String number;
    @NotNull
    @Min(1)
    private Integer dues;
}
