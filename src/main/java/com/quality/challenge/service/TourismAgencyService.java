package com.quality.challenge.service;

import com.quality.challenge.dto.HotelDTO;
import com.quality.challenge.dto.ListResponseDTO;
import com.quality.challenge.dto.StatusCodeDTO;
import com.quality.challenge.exceptions.InvalidDateException;
import com.quality.challenge.exceptions.InvalidDestinationException;
import com.quality.challenge.interfaces.IHotelRepository;
import com.quality.challenge.interfaces.ITourismAgencyService;
import com.quality.challenge.utils.DateUtil;
import com.quality.challenge.utils.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TourismAgencyService implements ITourismAgencyService {
    private final IHotelRepository hotelRepository;

    @Autowired
    public TourismAgencyService(IHotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
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

    private void validateFilters(Date dateFrom, Date dateTo, String destination) throws InvalidDateException, InvalidDestinationException {
        DateUtil.correctDateFromAndDateTo(dateFrom, dateTo);
        this.hotelRepository.containsDestination(destination);
    }
}
