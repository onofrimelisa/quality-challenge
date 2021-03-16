package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidDestinationException extends TourismAgencyException{
    public InvalidDestinationException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
