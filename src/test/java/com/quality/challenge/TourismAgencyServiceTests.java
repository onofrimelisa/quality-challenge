package com.quality.challenge;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.exceptions.InvalidDateException;
import com.quality.challenge.exceptions.InvalidDestinationException;
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
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

public class TourismAgencyServiceTests {
    private ITourismAgencyService tourismAgencyService;
    @Mock
    private IHotelRepository hotelRepository;
    @Mock
    private IFlightRepository flightRepository;

    @BeforeEach
    public void setContext(){
        initMocks(this);
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
        InvalidDestinationException expectedException = new InvalidDestinationException(StatusCodeUtil.getCustomStatusCode("The chosen destination does not exist", HttpStatus.NOT_FOUND));
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
        InvalidDateException expectedException = new InvalidDateException(StatusCodeUtil.getCustomStatusCode("The dateFrom field must be smaller than the dateTo field", HttpStatus.BAD_REQUEST));

        // Act & Assert
        InvalidDateException thrownException = Assertions.assertThrows(
                InvalidDateException.class,
                () -> this.tourismAgencyService.getHotels(new Date("03/04/2021"), new Date("03/02/2021"), Mockito.anyString())
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrownException.getStatusCodeDTO().getStatus());
    }

    private List<HotelDTO> getHotelsDTO(){
        HotelDTO hotel1 = new HotelDTO();
        hotel1.setAvailableFrom(new Date("02/02/2021"));
        hotel1.setAvailableTo(new Date(("04/04/2021")));
        hotel1.setBooked(false);
        hotel1.setCity("foo");
        hotel1.setCode("bar");
        hotel1.setName("baz");
        hotel1.setRoomType("quz");
        hotel1.setPrice(100d);

        HotelDTO hotel2 = new HotelDTO();
        hotel2.setAvailableFrom(new Date("02/04/2021"));
        hotel2.setAvailableTo(new Date(("04/06/2021")));
        hotel2.setBooked(false);
        hotel2.setCode("foo");
        hotel2.setCity("bar");
        hotel2.setName("baz");
        hotel2.setRoomType("quz");
        hotel2.setPrice(100d);

        List<HotelDTO> hotels = new ArrayList<>();
        hotels.add(hotel1);
        hotels.add(hotel2);

        return hotels;
    }
}
