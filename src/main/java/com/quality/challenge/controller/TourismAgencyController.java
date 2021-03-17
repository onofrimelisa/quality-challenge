package com.quality.challenge.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.quality.challenge.dto.*;
import com.quality.challenge.exceptions.*;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.utils.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
@Validated
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
        @Valid @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull LocalDate dateTo,
        @Valid @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) @NotNull LocalDate dateFrom,
        @Valid @RequestParam @NotEmpty String destination
    ) throws InvalidDateException, InvalidDestinationException {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date dateToFilter = Date.from(dateTo.atStartOfDay(defaultZoneId).toInstant());
        Date dateFromFilter = Date.from(dateFrom.atStartOfDay(defaultZoneId).toInstant());

        return this.tourismAgencyService.getHotels(dateFromFilter, dateToFilter, destination);
    }

    @PostMapping("/booking")
    public BookingResponseDTO bookRoom(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) throws InvalidDateException, InvalidDestinationException, InvalidPeopleForRoomException, UnavailableHotelException {
        return this.tourismAgencyService.bookRoom(bookingRequestDTO);
    }

    /* #######################################################################################################

                                            HANDLERS

     ######################################################################################################### */

    @ExceptionHandler(TourismAgencyException.class)
    public ResponseEntity<StatusCodeDTO> handleTourismAgencyException(TourismAgencyException searchEngineException) {
        return new ResponseEntity<>(searchEngineException.getStatusCodeDTO(), searchEngineException.getStatusCodeDTO().getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, InvalidFormatException.class})
    public ResponseEntity<StatusCodeDTO> handleTypeMismatchException(Exception e) {
        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The date must have the format dd/mm/yyyy", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(statusCodeDTO, statusCodeDTO.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<StatusCodeDTO> handleConstraintViolationException(Exception constraintViolationException) {
        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The parameters sent are not valid", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(statusCodeDTO, statusCodeDTO.getStatus());
    }
}
