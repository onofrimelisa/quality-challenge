package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class UnavailableFligthException extends TourismAgencyException{
    public UnavailableFligthException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
