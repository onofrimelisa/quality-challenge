package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidDateException extends TourismAgencyException{
    public InvalidDateException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
