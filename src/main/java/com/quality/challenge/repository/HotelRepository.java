package com.quality.challenge.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.ServerErrorException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.utils.Filter;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@Getter
public class HotelRepository implements IHotelRepository {
    private final List<HotelDTO> hotels;

    public HotelRepository() throws ServerErrorException {
        this.hotels = loadDatabase();
    }

    @Override
    public List<HotelDTO> loadDatabase() throws ServerErrorException {
        File file;

        try{
            file = ResourceUtils.getFile("classpath:hotels.json");
        }catch (FileNotFoundException e){
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<HotelDTO>> typeRef = new TypeReference<>() {};
        List<HotelDTO> hotels;

        try{
            hotels = objectMapper.readValue(file, typeRef);
        }catch (Exception e) {
            StatusCodeDTO statusCodeDTO = new StatusCodeDTO("Cannot establish connection with the server", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new ServerErrorException(statusCodeDTO);
        }

        return hotels;
    }

    @Override
    public List<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination) {
        List<HotelDTO> hotelsList = this.hotels;
        List<Predicate<HotelDTO>> allPredicates = new ArrayList<>();

        allPredicates.add(Filter.filterHotelsByDateTo(dateTo));
        allPredicates.add(Filter.filterHotelsByDateFrom(dateFrom));
        allPredicates.add(Filter.filterHotelsByDestination(destination));
        allPredicates.add(Filter.filterHotelsByBooking(false));

        return hotelsList
            .stream()
            .filter(allPredicates.stream().reduce(x->true, Predicate::and))
            .collect(Collectors.toList());
    }

    @Override
    public Boolean containsDestination(String destiny) {
        return this.hotels
            .stream()
            .map(HotelDTO::getCity)
            .collect(Collectors.toList())
            .stream()
            .anyMatch(destiny::equalsIgnoreCase);
    }
}
