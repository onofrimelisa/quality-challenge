package com.quality.challenge.interfaces;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.exceptions.InvalidDateException;
import com.quality.challenge.exceptions.InvalidDestinationException;

import java.util.Date;


public interface ITourismAgencyService {
    ListResponseDTO<HotelDTO> getHotels();
    ListResponseDTO<HotelDTO> getHotels(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException;
}
