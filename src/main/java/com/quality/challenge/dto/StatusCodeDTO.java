package com.quality.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class StatusCodeDTO {
    private String message;
    private HttpStatus status;
}
