package com.quality.challenge;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.exceptions.ServerErrorException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

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
}
