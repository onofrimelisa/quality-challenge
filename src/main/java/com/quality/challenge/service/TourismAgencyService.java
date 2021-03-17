package com.quality.challenge.service;

import com.quality.challenge.dto.*;
import com.quality.challenge.exceptions.InvalidDateException;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.InvalidPeopleForRoomException;
import com.quality.challenge.exceptions.UnavailableHotelException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.utils.DateUtil;
import com.quality.challenge.utils.RoomUtil;
import com.quality.challenge.utils.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TourismAgencyService implements ITourismAgencyService {
    private final IHotelRepository hotelRepository;

    @Autowired
    public TourismAgencyService(IHotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public ListResponseDTO<HotelDTO> getHotels() {
        ListResponseDTO<HotelDTO> response = new ListResponseDTO<>();
        List<HotelDTO> hotels = this.hotelRepository.getHotels();
        response.setList(hotels);
        response.setTotal(hotels.size());
        response.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
        return response;
    }

    @Override
    public ListResponseDTO<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException {
        validateFilters(dateFrom, dateTo, destination);

        ListResponseDTO<HotelDTO> response = new ListResponseDTO<>();
        List<HotelDTO> hotels = this.hotelRepository.getHotels(dateFrom, dateTo, destination);
        response.setList(hotels);
        response.setTotal(hotels.size());
        response.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
        return response;
    }

    @Override
    public BookingResponseDTO bookRoom(BookingRequestDTO bookingRequestDTO) throws InvalidPeopleForRoomException, InvalidDestinationException, InvalidDateException, UnavailableHotelException {
        BookingDTO booking = bookingRequestDTO.getBooking();
        validateFilters(booking.getDateFrom(), booking.getDateTo(), booking.getDestination());
        RoomUtil.correctNumberOfPeopleForRoom(booking.getRoomType(), booking.getPeopleAmount(), booking.getPeople().size());

        if(this.hotelRepository.hasAvailability(booking.getHotelCode(), booking.getDestination(), booking.getDateFrom(), booking.getDateTo(), booking.getRoomType())){
            this.hotelRepository.bookHotel(booking.getHotelCode());
            BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
            bookingResponseDTO.setBooking(booking);
            bookingResponseDTO.setTotal(1d);
            bookingResponseDTO.setAmount(1d);
            bookingResponseDTO.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
            bookingResponseDTO.setInterest(1d);
            bookingResponseDTO.setUsername(bookingRequestDTO.getUsername());
            return bookingResponseDTO;
        }

        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen hotel has no availability for the dates, destination and room type selected", HttpStatus.BAD_REQUEST);
        throw new UnavailableHotelException(statusCodeDTO);
    }

    private void validateFilters(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException {
        DateUtil.correctDateFromAndDateTo(dateFrom, dateTo);
        this.hotelRepository.containsDestination(destination);
    }
}
