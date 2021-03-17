package com.quality.challenge.exceptions;

import com.quality.challenge.dto.StatusCodeDTO;

public class InvalidPeopleForRoomException extends TourismAgencyException{
    public InvalidPeopleForRoomException(StatusCodeDTO statusCodeDTO) {
        super(statusCodeDTO);
    }
}
