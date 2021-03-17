package com.quality.challenge.interfaces;

import com.quality.challenge.dto.BookingRequestDTO;
import com.quality.challenge.dto.BookingResponseDTO;
import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.exceptions.*;

import java.util.Date;


public interface ITourismAgencyService {
    ListResponseDTO<HotelDTO> getHotels();
    ListResponseDTO<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException;
    BookingResponseDTO bookRoom(BookingRequestDTO bookingRequestDTO) throws InvalidPeopleForRoomException, InvalidDestinationException, InvalidDateException, UnavailableHotelException, InvalidCardDuesException;
}
