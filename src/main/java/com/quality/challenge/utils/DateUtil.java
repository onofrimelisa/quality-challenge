package com.quality.challenge.utils;

import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDateException;
import org.springframework.http.HttpStatus;

import java.util.Date;

public class DateUtil {
    public static void correctDateFromAndDateTo(Date dateFrom, Date dateTo) throws InvalidDateException {
        if (!dateFrom.before(dateTo)){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The dateFrom field must be prior to the dateTo field", HttpStatus.BAD_REQUEST);
            throw new InvalidDateException(statusCodeDTO);
        }

    }
}
