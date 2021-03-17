package com.quality.challenge.interfaces;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.exceptions.ServerErrorException;

import java.util.Date;
import java.util.List;

public interface IHotelRepository {
    List<HotelDTO> loadDatabase() throws ServerErrorException;
    List<HotelDTO> getHotels();
    List<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination);
    void containsDestination(String destiny) throws InvalidDestinationException;
    Boolean hasAvailability(String hotelCode, String destination, Date dateFrom, Date dateTo, String roomType);
    void bookHotel(String hotelCode);
}
