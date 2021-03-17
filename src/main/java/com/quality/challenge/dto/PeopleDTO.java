package com.quality.challenge.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PeopleDTO {
    @NotEmpty
    private String dni;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date birthDate;
    @NotEmpty
    @Email
    private String mail;
}
