package com.quality.challenge.utils;

import com.quality.challenge.dto.StatusCodeDTO;
import org.springframework.http.HttpStatus;

public class StatusCode {
    public static StatusCodeDTO getSuccessfulOperationStatusCode() {
        return new StatusCodeDTO("Operation performed successfully", HttpStatus.OK);
    }

    public static StatusCodeDTO getCustomStatusCode(String message, HttpStatus statusCode){
        return new StatusCodeDTO(message, statusCode);
    }
}
