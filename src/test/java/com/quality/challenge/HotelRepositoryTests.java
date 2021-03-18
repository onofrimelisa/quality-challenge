package com.quality.challenge;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.ServerErrorException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.repository.HotelRepository;
import com.quality.challenge.utils.StatusCodeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Optional;

class HotelRepositoryTests {
    private IHotelRepository hotelRepository;

    @BeforeEach
    public void setContext() throws ServerErrorException {
        this.hotelRepository = new HotelRepository();
    }

    @Test
    void getHotels() {
        // Act
        List<HotelDTO> result = this.hotelRepository.getHotels();

        // Assertions
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    void getHotelsWithFilters() {
        // Arrange
        List<HotelDTO> allHotels = this.hotelRepository.getHotels();
        Date dateFrom = new Date("11/02/2021");
        Date dateTo = new Date("02/03/2021");

        // Act
        List<HotelDTO> result = this.hotelRepository.getHotels(dateFrom, dateTo, "puerto iguazú");

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(allHotels, result);
        Assertions.assertNotEquals(allHotels.size(), result.size());
        Assertions.assertTrue(allHotels.size() > result.size());
    }

    @Test
    void containsDestinationSuccess(){
        // Act & Assert
        Assertions.assertDoesNotThrow(() -> this.hotelRepository.containsDestination("puerto iguazú"));
    }

    @Test
    void containsDestinationThrowsInvalidDestinationException(){
        // Arrange
        InvalidDestinationException expectedException = new InvalidDestinationException(StatusCodeUtil.getCustomStatusCode(
                "The chosen destination does not exist",
                HttpStatus.NOT_FOUND));

        // Act & Assert
        InvalidDestinationException thrownException = Assertions.assertThrows(
                InvalidDestinationException.class,
                () -> this.hotelRepository.containsDestination("invalid")
        );
        Assertions.assertEquals(expectedException.getMessage(), thrownException.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrownException.getStatusCodeDTO().getStatus());
    }

    @Test
    void hasAvailabilityWithPresentResult(){
        // Act
        Optional<HotelDTO> result = this.hotelRepository.hasAvailability("CH-0002", "puerto iguazú", new Date("02/11/2021"), new Date("03/19/2021"), "double");

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void hasAvailabilityWithEmptyResult(){
        // Act
        Optional<HotelDTO> result = this.hotelRepository.hasAvailability("foo", "bar", new Date("02/11/2021"), new Date("03/19/2021"), "baz");

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}
