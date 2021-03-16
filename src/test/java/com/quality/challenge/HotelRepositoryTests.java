package com.quality.challenge;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.exceptions.ServerErrorException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.repository.HotelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
