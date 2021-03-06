package com.quality.challenge;

import com.quality.challenge.dto.*;
import com.quality.challenge.exceptions.*;
import com.quality.challenge.interfaces.IFlightRepository;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.service.TourismAgencyService;
import com.quality.challenge.utils.StatusCodeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.MockitoAnnotations.openMocks;

public class TourismAgencyServiceTests {
    private ITourismAgencyService tourismAgencyService;
    @Mock
    private IHotelRepository hotelRepository;
    @Mock
    private IFlightRepository flightRepository;

    @BeforeEach
    public void setContext(){
        openMocks(this);
        tourismAgencyService = new TourismAgencyService(hotelRepository, flightRepository);
    }

    @Test
    void getHotels(){
        // Arrange
        List<HotelDTO> hotels = getHotelsDTO();
        Mockito.when(this.hotelRepository.getHotels()).thenReturn(hotels);

        // Act
        ListResponseDTO<HotelDTO> result = this.tourismAgencyService.getHotels();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertIterableEquals(hotels, result.getList());
        Assertions.assertEquals(hotels.size(), result.getTotal());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void getHotelsWithFiltersSuccess() throws InvalidDestinationException {
        // Arrange
        List<HotelDTO> hotels = getHotelsDTO();
        Mockito.doNothing().when(this.hotelRepository).containsDestination(Mockito.anyString());
        Mockito.when(this.hotelRepository.getHotels(Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(hotels);

        // Act & Assert
        ListResponseDTO<HotelDTO> result = Assertions.assertDoesNotThrow(
            () -> this.tourismAgencyService.getHotels(new Date("03/02/2021"), new Date("03/04/2021"), Mockito.anyString())
        );
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getList().size() > 0);
        Assertions.assertIterableEquals(hotels, result.getList());
        Assertions.assertEquals(hotels.size(), result.getTotal());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void getHotelsWithFiltersThrowsInvalidDestinationException() throws InvalidDestinationException {
        // Arrange
        InvalidDestinationException expectedException = new InvalidDestinationException(StatusCodeUtil.getCustomStatusCode(
                "The chosen destination does not exist",
                HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(this.hotelRepository).containsDestination(Mockito.anyString());

        // Act & Assert
        InvalidDestinationException thrownException = Assertions.assertThrows(
                InvalidDestinationException.class,
                () -> this.tourismAgencyService.getHotels(new Date("03/02/2021"), new Date("03/04/2021"), Mockito.anyString())
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void getHotelsWithFiltersThrowsInvalidDateException() {
        // Arrange
        InvalidDateException expectedException = new InvalidDateException(StatusCodeUtil.getCustomStatusCode(
                "The dateFrom field must be prior to the dateTo field",
                HttpStatus.BAD_REQUEST));

        // Act & Assert
        InvalidDateException thrownException = Assertions.assertThrows(
                InvalidDateException.class,
                () -> this.tourismAgencyService.getHotels(new Date("03/04/2021"), new Date("03/02/2021"), "puerto iguaz??")
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelSuccess(){
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");

        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "puerto iguaz??", "triple", 1);
        Mockito.when(this.hotelRepository.hasAvailability(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(getHotelDTO()));
        Mockito.doNothing().when(this.hotelRepository).bookHotel(Mockito.anyString());

        // Act & Assert
        BookingResponseDTO result = Assertions.assertDoesNotThrow(
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(bookingRequestDTO.getBooking(), result.getBooking());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelThrowsInvalidDestinationException() throws InvalidDestinationException {
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");

        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "invalid", "triple", 1);
        InvalidDestinationException expectedException = new InvalidDestinationException(StatusCodeUtil.getCustomStatusCode(
                "The chosen destination does not exist",
                HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(this.hotelRepository).containsDestination(Mockito.anyString());

        // Act & Assert
        InvalidDestinationException thrownException = Assertions.assertThrows(
                InvalidDestinationException.class,
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelThrowsInvalidDateException() {
        // Arrange
        Date dateTo = new Date("02/01/2021");
        Date dateFrom = new Date("03/03/2021");

        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "puerto iguaz??", "triple", 1);
        InvalidDateException expectedException = new InvalidDateException(StatusCodeUtil.getCustomStatusCode(
                "The dateFrom field must be prior to the dateTo field",
                HttpStatus.BAD_REQUEST));

        // Act & Assert
        InvalidDateException thrownException = Assertions.assertThrows(
                InvalidDateException.class,
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelThrowsInvalidPeopleForRoomException() {
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");

        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "puerto iguaz??", "double", 1);
        InvalidPeopleForRoomException expectedException = new InvalidPeopleForRoomException(StatusCodeUtil.getCustomStatusCode(
                "The amount of people is incorrect for the chosen room, or the people sent is different from the amount of people value sent",
                HttpStatus.BAD_REQUEST));

        // Act & Assert
        InvalidPeopleForRoomException thrownException = Assertions.assertThrows(
                InvalidPeopleForRoomException.class,
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelThrowsUnavailableHotelException(){
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");
        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "puerto iguaz??", "triple", 1);

        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode("" +
                "The chosen hotel has no availability for the dates, destination and room type selected",
                HttpStatus.BAD_REQUEST);
        UnavailableHotelException expectedException = new UnavailableHotelException(statusCodeDTO);
        Mockito.when(this.hotelRepository.hasAvailability(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UnavailableHotelException thrownException = Assertions.assertThrows(
                UnavailableHotelException.class,
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookHotelThrowsInvalidCardDuesException(){
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");
        BookingRequestDTO bookingRequestDTO = getBookingRequestDTO(dateFrom, dateTo, "puerto iguaz??", "triple", 0);

        StatusCodeDTO statusCodeDTO = StatusCodeUtil.getCustomStatusCode(
                "The dues are not valid for the selected type of card. For credit cards, the valid dues are: 1, 3, 6 and 9; for debit cards, the valid dues are: 1",
                HttpStatus.BAD_REQUEST);
        InvalidCardDuesException expectedException = new InvalidCardDuesException(statusCodeDTO);
        Mockito.when(this.hotelRepository.hasAvailability(Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(getHotelDTO()));
        Mockito.doNothing().when(this.hotelRepository).bookHotel(Mockito.anyString());

        // Act & Assert
        InvalidCardDuesException thrownException = Assertions.assertThrows(
                InvalidCardDuesException.class,
                () -> this.tourismAgencyService.bookHotel(bookingRequestDTO)
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void getFlights(){
        // Arrange
        List<FlightDTO> flights = getFlightsDTO();
        Mockito.when(this.flightRepository.getFlights()).thenReturn(flights);

        // Act
        ListResponseDTO<FlightDTO> result = this.tourismAgencyService.getFlights();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertIterableEquals(flights, result.getList());
        Assertions.assertEquals(flights.size(), result.getTotal());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void getFlightsWithFiltersSuccess() throws InvalidDestinationException {
        // Arrange
        List<FlightDTO> flights = getFlightsDTO();
        Mockito.doNothing().when(this.flightRepository).containsDestination(Mockito.anyString());
        Mockito.when(this.flightRepository.getFlights(Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(flights);

        // Act & Assert
        ListResponseDTO<FlightDTO> result = Assertions.assertDoesNotThrow(
                () -> this.tourismAgencyService.getFlights(new Date("03/02/2021"), new Date("03/04/2021"), "foo", "bar")
        );
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getList().size() > 0);
        Assertions.assertIterableEquals(flights, result.getList());
        Assertions.assertEquals(flights.size(), result.getTotal());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }

    @Test
    void getFlightsWithFiltersThrowsInvalidDestinationException() throws InvalidDestinationException {
        // Arrange
        InvalidDestinationException expectedException = new InvalidDestinationException(StatusCodeUtil.getCustomStatusCode(
                "The chosen destination does not exist",
                HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(this.flightRepository).containsDestination(Mockito.anyString());

        // Act & Assert
        InvalidDestinationException thrownException = Assertions.assertThrows(
                InvalidDestinationException.class,
                () -> this.tourismAgencyService.getFlights(new Date("03/02/2021"), new Date("03/04/2021"), "foo", "bar")
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void getFlightsWithFiltersThrowsInvalidOriginException() throws InvalidOriginException {
        // Arrange
        InvalidOriginException expectedException = new InvalidOriginException(StatusCodeUtil.getCustomStatusCode(
                "The chosen origin does not exist",
                HttpStatus.NOT_FOUND));
        Mockito.doThrow(expectedException).when(this.flightRepository).containsOrigin(Mockito.anyString());

        // Act & Assert
        InvalidOriginException thrownException = Assertions.assertThrows(
                InvalidOriginException.class,
                () -> this.tourismAgencyService.getFlights(new Date("03/02/2021"), new Date("03/04/2021"), "foo", "bar")
        );
        Assertions.assertEquals(expectedException, thrownException);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void getFlightsWithFiltersThrowsInvalidDateException() {
        // Arrange
        InvalidDateException expectedException = new InvalidDateException(StatusCodeUtil.getCustomStatusCode(
                "The dateFrom field must be prior to the dateTo field",
                HttpStatus.BAD_REQUEST));

        // Act & Assert
        InvalidDateException thrownException = Assertions.assertThrows(
                InvalidDateException.class,
                () -> this.tourismAgencyService.getFlights(new Date("03/04/2021"), new Date("03/02/2021"), "foo", "bar")
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void bookFlightSuccess(){
        // Arrange
        Date dateFrom = new Date("02/01/2021");
        Date dateTo = new Date("03/03/2021");

        FlightReservationRequestDTO flightReservationRequestDTO = getFlightReservationRequestDTO(dateFrom, dateTo, "buenos aires", "puerto iguaz??", 1);
        Mockito.when(this.flightRepository.hasAvailability(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(getFlightDTO()));

        // Act & Assert
        FlightReservationResponseDTO result = Assertions.assertDoesNotThrow(
                () -> this.tourismAgencyService.bookFlight(flightReservationRequestDTO)
        );
        Assertions.assertEquals(flightReservationRequestDTO.getFlightReservation(), result.getFlightReservation());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCodeDTO().getStatus());
    }


    private BookingRequestDTO getBookingRequestDTO(Date dateFrom, Date dateTo, String destination, String roomType, Integer cardDues){
        List<PeopleDTO> people = getPeopleDTO();
        PaymentMethodDTO paymentMethodDTO = getPaymentMethodDTO(cardDues);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setPaymentMethod(paymentMethodDTO);
        bookingDTO.setRoomType(roomType);
        bookingDTO.setDateFrom(dateFrom);
        bookingDTO.setDateTo(dateTo);
        bookingDTO.setHotelCode("foo");
        bookingDTO.setPeople(people);
        bookingDTO.setDestination(destination);
        bookingDTO.setPeopleAmount(people.size());

        BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
        bookingRequestDTO.setUsername("foo@bar.baz");
        bookingRequestDTO.setBooking(bookingDTO);

        return bookingRequestDTO;
    }

    private List<PeopleDTO> getPeopleDTO(){
        PeopleDTO person1 = new PeopleDTO();
        person1.setName("foo");
        person1.setBirthDate(new Date("02/05/1986"));
        person1.setDni("quz");
        person1.setMail("foo@bar.baz");

        PeopleDTO person2 = new PeopleDTO();
        person2.setName("foo");
        person2.setBirthDate(new Date("02/05/1986"));
        person2.setDni("quz");
        person2.setMail("foo@bar.baz");

        PeopleDTO person3 = new PeopleDTO();
        person3.setName("foo");
        person3.setBirthDate(new Date("02/05/1986"));
        person3.setDni("quz");
        person3.setMail("foo@bar.baz");

        List<PeopleDTO> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);

        return people;
    }

    private PaymentMethodDTO getPaymentMethodDTO(Integer cardDues){
        PaymentMethodDTO paymentMethodDTO= new PaymentMethodDTO();
        paymentMethodDTO.setDues(cardDues);
        paymentMethodDTO.setType("debit");
        paymentMethodDTO.setNumber("456789456789");

        return paymentMethodDTO;
    }

    private List<HotelDTO> getHotelsDTO(){
        List<HotelDTO> hotels = new ArrayList<>();
        hotels.add(getHotelDTO());
        hotels.add(getHotelDTO());

        return hotels;
    }

    private HotelDTO getHotelDTO(){
        HotelDTO hotel = new HotelDTO();
        hotel.setAvailableFrom(new Date("02/02/2021"));
        hotel.setAvailableTo(new Date("04/04/2021"));
        hotel.setBooked(false);
        hotel.setCity("foo");
        hotel.setCode("bar");
        hotel.setName("baz");
        hotel.setRoomType("quz");
        hotel.setPrice(100d);

        return hotel;
    }

    private FlightReservationRequestDTO getFlightReservationRequestDTO(Date dateFrom, Date dateTo, String origin, String destination, Integer cardDues){
        FlightReservationDTO flightReservationDTO = getFlightReservationDTO(dateFrom, dateTo, origin, destination, cardDues);

        FlightReservationRequestDTO flightReservationRequestDTO = new FlightReservationRequestDTO();
        flightReservationRequestDTO.setUsername("foo@bar.baz");
        flightReservationRequestDTO.setFlightReservation(flightReservationDTO);

        return flightReservationRequestDTO;
    }

    private List<FlightDTO> getFlightsDTO(){
        List<FlightDTO> flights = new ArrayList<>();
        flights.add(getFlightDTO());
        flights.add(getFlightDTO());
        flights.add(getFlightDTO());

        return flights;
    }

    private FlightReservationDTO getFlightReservationDTO(Date dateFrom, Date dateTo, String origin, String destination, Integer cardDues){
        List<PeopleDTO> people = getPeopleDTO();

        FlightReservationDTO flightReservationDTO = new FlightReservationDTO();
        flightReservationDTO.setFlightNumber("foo");
        flightReservationDTO.setDateFrom(dateFrom);
        flightReservationDTO.setDateTo(dateTo);
        flightReservationDTO.setOrigin(origin);
        flightReservationDTO.setDestination(destination);
        flightReservationDTO.setPaymentMethod(getPaymentMethodDTO(cardDues));
        flightReservationDTO.setPeople(people);
        flightReservationDTO.setSeats(people.size());
        flightReservationDTO.setSeatType("bar");

        return flightReservationDTO;
    }

    private FlightDTO getFlightDTO(){
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setDateFrom(new Date("02/02/2021"));
        flightDTO.setDateTo(new Date("04/04/2021"));
        flightDTO.setFlightNumber("foo");
        flightDTO.setDestination("bar");
        flightDTO.setOrigin("baz");
        flightDTO.setSeatType("quz");
        flightDTO.setPrice(100d);

        return flightDTO;
    }
}
