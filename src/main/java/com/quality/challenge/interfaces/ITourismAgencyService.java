package com.quality.challenge.interfaces;

import com.quality.challenge.dto.*;
import com.quality.challenge.exceptions.*;

import java.util.Date;


public interface ITourismAgencyService {
    ListResponseDTO<HotelDTO> getHotels();
    ListResponseDTO<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException;
    BookingResponseDTO bookHotel(BookingRequestDTO bookingRequestDTO) throws InvalidPeopleForRoomException, InvalidDestinationException, InvalidDateException, UnavailableHotelException, InvalidCardDuesException;
    ListResponseDTO<FlightDTO> getFlights();
    ListResponseDTO<FlightDTO> getFlights(Date dateFrom, Date dateTo, String origin, String destination) throws InvalidDateException, InvalidOriginException, InvalidDestinationException;
    FlightReservationResponseDTO bookFlight(FlightReservationRequestDTO reservationRequestDTO) throws InvalidDateException, InvalidOriginException, InvalidDestinationException, InvalidPeopleForFlightException, InvalidCardDuesException, UnavailableFligthException;
}
