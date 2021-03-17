package com.quality.challenge.utils;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class DestinationUtil {
    public static void containsDestination(List<HotelDTO> hotels, String destination) throws InvalidDestinationException {
        if(Boolean.TRUE.equals(
            hotels
            .stream()
            .map(HotelDTO::getCity)
            .collect(Collectors.toList())
            .stream()
            .noneMatch(destination::equalsIgnoreCase))
        ){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen destination does not exist", HttpStatus.NOT_FOUND);
            throw new InvalidDestinationException(statusCodeDTO);
        }
    }
}
