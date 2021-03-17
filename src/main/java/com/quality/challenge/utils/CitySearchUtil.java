package com.quality.challenge.utils;

import com.quality.challenge.dto.FlightDTO;
import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.InvalidOriginException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

public class CitySearchUtil {
    public static void hotelContainsDestination(List<HotelDTO> hotels, String destination) throws InvalidDestinationException {
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

    public static void flightContainsDestination(List<FlightDTO> flights, String destination) throws InvalidDestinationException {
        if(Boolean.TRUE.equals(
            flights
                .stream()
                .map(FlightDTO::getDestination)
                .collect(Collectors.toList())
                .stream()
                .noneMatch(destination::equalsIgnoreCase))
        ){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen destination does not exist", HttpStatus.NOT_FOUND);
            throw new InvalidDestinationException(statusCodeDTO);
        }
    }

    public static void flightContainsOrigin(List<FlightDTO> flights, String origin) throws InvalidOriginException {
        if(Boolean.TRUE.equals(
            flights
                .stream()
                .map(FlightDTO::getOrigin)
                .collect(Collectors.toList())
                .stream()
                .noneMatch(origin::equalsIgnoreCase))
        ){
            StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen origin does not exist", HttpStatus.NOT_FOUND);
            throw new InvalidOriginException(statusCodeDTO);
        }
    }
}
