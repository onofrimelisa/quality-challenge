package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class TourismAgencyException extends Exception{
    private StatusCodeDTO statusCodeDTO;

    public TourismAgencyException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO.getMessage());
        this.statusCodeDTO = statusCodeDTO;
    }
}
