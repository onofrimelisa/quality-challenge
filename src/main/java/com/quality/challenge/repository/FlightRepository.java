package com.quality.challenge.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quality.challenge.dto.FlightDTO;
import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.InvalidOriginException;
import com.quality.challenge.exceptions.ServerErrorException;
import com.quality.challenge.interfaces.IFlightRepository;
import com.quality.challenge.utils.CitySearchUtil;
import com.quality.challenge.utils.FilterUtil;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Repository
public class FlightRepository implements IFlightRepository {
    private final List<FlightDTO> flights;

    public FlightRepository() throws ServerErrorException {
        this.flights = this.loadDatabase();
    }

    @Override
    public List<FlightDTO> loadDatabase() throws ServerErrorException {
        File file;

        try{
            file = ResourceUtils.getFile("classpath:flights.json");
        }catch (FileNotFoundException e){
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<FlightDTO>> typeRef = new TypeReference<>() {};
        List<FlightDTO> flights;

        try{
            flights = objectMapper.readValue(file, typeRef);
        }catch (Exception e) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        return flights;
    }

    @Override
    public List<FlightDTO> getFlights(Date dateFrom, Date dateTo, String origin, String destination) {
        List<Predicate<FlightDTO>> allPredicates = new ArrayList<>();

        allPredicates.add(FilterUtil.filterFlightsByDateTo(dateTo));
        allPredicates.add(FilterUtil.filterFlightsByDateFrom(dateFrom));
        allPredicates.add(FilterUtil.filterFlightsByDestination(destination));
        allPredicates.add(FilterUtil.filterFlightsByOrigin(origin));

        return this.flights
            .stream()
            .filter(allPredicates.stream().reduce(x->true, Predicate::and))
            .collect(Collectors.toList());
    }

    @Override
    public void containsDestination(String destination) throws InvalidDestinationException {
        CitySearchUtil.flightContainsDestination(this.flights, destination);
    }

    @Override
    public void containsOrigin(String origin) throws InvalidOriginException {
        CitySearchUtil.flightContainsOrigin(this.flights, origin);
    }
}
