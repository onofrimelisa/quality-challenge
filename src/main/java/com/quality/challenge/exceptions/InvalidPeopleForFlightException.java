package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidPeopleForFlightException extends TourismAgencyException{
    public InvalidPeopleForFlightException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
