package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class UnavailableHotelException extends TourismAgencyException{
    public UnavailableHotelException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
