package com.quality.challenge.interfaces;

import com.quality.challenge.dto.FlightDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.InvalidOriginException;
import com.quality.challenge.exceptions.ServerErrorException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface IFlightRepository {
    List<FlightDTO> loadDatabase() throws ServerErrorException;
    List<FlightDTO> getFlights();
    List<FlightDTO> getFlights(Date dateFrom, Date dateTo, String origin, String destination);
    void containsDestination(String destination) throws InvalidDestinationException;
    void containsOrigin(String origin) throws InvalidOriginException;
    Optional<FlightDTO> hasAvailability(String flightNumber, String origin, String destination, Date dateFrom, Date dateTo, String seatType);
}
