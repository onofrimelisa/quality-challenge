package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class ServerErrorException extends TourismAgencyException {
    public ServerErrorException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
