package com.quality.challenge.controller;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDateException;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.TourismAgencyException;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class TourismAgencyController {
    private final ITourismAgencyService tourismAgencyService;
    private final static String DATE_FORMAT = "dd/MM/yyyy";

    @Autowired
    public TourismAgencyController(ITourismAgencyService tourismAgencyService) {
        this.tourismAgencyService = tourismAgencyService;
    }

    @GetMapping("/hotels")
    public ListResponseDTO<HotelDTO> getHotels(){
        return this.tourismAgencyService.getHotels();
    }

    @GetMapping(value = "/hotels", params = {"dateFrom", "dateTo", "destination"})
    public ListResponseDTO<HotelDTO> getHotels(
        @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull LocalDate dateTo,
        @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull LocalDate dateFrom,
        @RequestParam @NotNull String destination
    ) throws InvalidDateException, InvalidDestinationException {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date dateToFilter = Date.from(dateTo.atStartOfDay(defaultZoneId).toInstant());
        Date dateFromFilter = Date.from(dateFrom.atStartOfDay(defaultZoneId).toInstant());

        return this.tourismAgencyService.getHotels(dateFromFilter, dateToFilter, destination);
    }

    @ExceptionHandler(TourismAgencyException.class)
    public ResponseEntity<StatusCodeDTO> handleTourismAgencyException(TourismAgencyException searchEngineException) {
        return new ResponseEntity<>(searchEngineException.getStatusCodeDTO(), searchEngineException.getStatusCodeDTO().getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StatusCodeDTO> handleTypeMismatchException() {
        StatusCodeDTO statusCodeDTO = StatusCode.getCustomStatusCode("The date must have the format dd/mm/yyyy", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(statusCodeDTO, statusCodeDTO.getStatus());
    }
}
