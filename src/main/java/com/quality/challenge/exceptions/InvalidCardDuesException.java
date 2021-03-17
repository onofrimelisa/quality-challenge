package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidCardDuesException extends TourismAgencyException{
    public InvalidCardDuesException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
