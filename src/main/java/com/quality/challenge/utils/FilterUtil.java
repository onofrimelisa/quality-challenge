package com.quality.challenge.utils;

import com.quality.challenge.dto.HotelDTO;

import java.util.Date;
import java.util.function.Predicate;

public class FilterUtil {

    // HOTEL FILTERS

    public static Predicate<HotelDTO> filterHotelsByDateTo(Date dateTo){
        return hotelDTO -> hotelDTO.getAvailableTo().equals(dateTo) || hotelDTO.getAvailableTo().after(dateTo);
    }

    public static Predicate<HotelDTO> filterHotelsByDateFrom(Date dateFrom){
        return hotelDTO -> hotelDTO.getAvailableFrom().equals(dateFrom) || hotelDTO.getAvailableFrom().before(dateFrom);
    }

    public static Predicate<HotelDTO> filterHotelsByDestination(String destination){
        return hotelDTO -> hotelDTO.getCity().equalsIgnoreCase(destination);
    }

    public static Predicate<HotelDTO> filterHotelsByBooking(Boolean booked){
        return hotelDTO -> hotelDTO.getBooked().equals(booked);
    }
}
