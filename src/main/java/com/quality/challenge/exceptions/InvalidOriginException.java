package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidOriginException extends TourismAgencyException{
    public InvalidOriginException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
