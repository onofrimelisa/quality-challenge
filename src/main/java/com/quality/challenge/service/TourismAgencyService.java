package com.quality.challenge.service;

import com.quality.challenge.dto.*;
import com.quality.challenge.exceptions.*;
import com.quality.challenge.interfaces.IFlightRepository;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.utils.CardUtil;
import com.quality.challenge.utils.DateUtil;
import com.quality.challenge.utils.PeopleManagementUtil;
import com.quality.challenge.utils.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TourismAgencyService implements ITourismAgencyService {
    private final IHotelRepository hotelRepository;
    private final IFlightRepository flightRepository;

    @Autowired
    public TourismAgencyService(IHotelRepository hotelRepository, IFlightRepository flightRepository) {
        this.hotelRepository = hotelRepository;
        this.flightRepository = flightRepository;
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
    public BookingResponseDTO bookHotel(BookingRequestDTO bookingRequestDTO) throws InvalidPeopleForRoomException, InvalidDestinationException, InvalidDateException, UnavailableHotelException, InvalidCardDuesException {
        BookingDTO booking = bookingRequestDTO.getBooking();
        validateFilters(booking.getDateFrom(), booking.getDateTo(), booking.getDestination());
        PeopleManagementUtil.correctNumberOfPeopleForRoom(booking.getRoomType(), booking.getPeopleAmount(), booking.getPeople().size());

        Optional<HotelDTO> hotelWithAvailability = this.hotelRepository.hasAvailability(
            booking.getHotelCode(),
            booking.getDestination(),
            booking.getDateFrom(),
            booking.getDateTo(),
            booking.getRoomType()
        );

        if(hotelWithAvailability.isPresent()){
            this.hotelRepository.bookHotel(booking.getHotelCode());
            BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
            bookingResponseDTO.setBooking(booking);
            Double amount = calculateAmount(hotelWithAvailability.get(), booking.getDateFrom(), booking.getDateTo());
            bookingResponseDTO.setAmount(amount);
            Double interest = calculateInterest(booking.getPaymentMethod());
            bookingResponseDTO.setInterest(interest);
            bookingResponseDTO.setTotal(calculateTotal(amount, interest));
            bookingResponseDTO.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
            bookingResponseDTO.setUsername(bookingRequestDTO.getUsername());
            return bookingResponseDTO;
        }

        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen hotel has no availability for the dates, destination and room type selected", HttpStatus.BAD_REQUEST);
        throw new UnavailableHotelException(statusCodeDTO);
    }

    @Override
    public ListResponseDTO<FlightDTO> getFlights() {
        ListResponseDTO<FlightDTO> response = new ListResponseDTO<>();
        List<FlightDTO> flights = this.flightRepository.getFlights();
        response.setList(flights);
        response.setTotal(flights.size());
        response.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
        return response;
    }

    @Override
    public ListResponseDTO<FlightDTO> getFlights(Date dateFrom, Date dateTo, String origin, String destination) throws InvalidDateException, InvalidOriginException, InvalidDestinationException {
        validateFilters(dateFrom, dateTo, destination, origin);

        ListResponseDTO<FlightDTO> response = new ListResponseDTO<>();
        List<FlightDTO> flights = this.flightRepository.getFlights(dateFrom, dateTo, origin, destination);
        response.setList(flights);
        response.setTotal(flights.size());
        response.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
        return response;
    }

    @Override
    public FlightReservationResponseDTO bookFlight(FlightReservationRequestDTO reservationRequestDTO) throws InvalidDateException, InvalidOriginException, InvalidDestinationException, InvalidPeopleForFlightException, InvalidCardDuesException, UnavailableFligthException {
        FlightReservationDTO flightReservation = reservationRequestDTO.getFlightReservation();
        validateFilters(flightReservation.getDateFrom(), flightReservation.getDateTo(), flightReservation.getDestination(), flightReservation.getOrigin());
        PeopleManagementUtil.correctNumberOfPeopleForFlight(flightReservation.getSeats(), flightReservation.getPeople().size());

        Optional<FlightDTO> flightWithAvailability = this.flightRepository.hasAvailability(
            flightReservation.getFlightNumber(),
            flightReservation.getOrigin(),
            flightReservation.getDestination(),
            flightReservation.getDateFrom(),
            flightReservation.getDateTo(),
            flightReservation.getSeatType()
        );

        if(flightWithAvailability.isPresent()){
            FlightReservationResponseDTO flightReservationResponseDTO = new FlightReservationResponseDTO();
            flightReservationResponseDTO.setFlightReservation(flightReservation);
            Double amount = calculateAmount(flightWithAvailability.get(), flightReservation.getSeats());
            flightReservationResponseDTO.setAmount(amount);
            Double interest = calculateInterest(flightReservation.getPaymentMethod());
            flightReservationResponseDTO.setInterest(interest);
            flightReservationResponseDTO.setTotal(calculateTotal(amount, interest));
            flightReservationResponseDTO.setStatusCodeDTO(StatusCodeUtil.getSuccessfulOperationStatusCode());
            flightReservationResponseDTO.setUsername(reservationRequestDTO.getUsername());
            return flightReservationResponseDTO;
        }

        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("The chosen flight has no availability for the dates, destination, origin and seat type selected", HttpStatus.BAD_REQUEST);
        throw new UnavailableFligthException(statusCodeDTO);
    }

    private void validateFilters(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException {
        DateUtil.correctDateFromAndDateTo(dateFrom, dateTo);
        this.hotelRepository.containsDestination(destination);
    }

    private void validateFilters(Date dateFrom, Date dateTo, String destination, String origin) throws InvalidDateException, InvalidDestinationException, InvalidOriginException {
        DateUtil.correctDateFromAndDateTo(dateFrom, dateTo);
        this.flightRepository.containsDestination(destination);
        this.flightRepository.containsOrigin(origin);
    }

    private Double calculateAmount(HotelDTO hotel, Date dateFrom, Date dateTo){
        Long daysBetweenDates = DateUtil.calculateDaysBetweenDates(dateFrom, dateTo);
        return hotel.getPrice() * daysBetweenDates;
    }

    private Double calculateAmount(FlightDTO flight, Integer seats){
        return flight.getPrice() * seats;
    }

    private Double calculateInterest(PaymentMethodDTO paymentMethod) throws InvalidCardDuesException {
        CardUtil.verifyValidCardDues(paymentMethod.getType(), paymentMethod.getDues());
        return CardUtil.getInterest(paymentMethod.getDues());
    }

    private Double calculateTotal(Double amount, Double interest){
        return amount + (interest * amount / 100);
    }
}
