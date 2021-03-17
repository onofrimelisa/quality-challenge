package com.quality.challenge.utils;

import com.quality.challenge.dto.FlightDTO;
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

    // FLIGHTS FILTERS

    public static Predicate<FlightDTO> filterFlightsByDateTo(Date dateTo){
        return flightDTO -> flightDTO.getDateTo().equals(dateTo) || flightDTO.getDateTo().before(dateTo);
    }

    public static Predicate<FlightDTO> filterFlightsByDateFrom(Date dateFrom){
        return flightDTO -> flightDTO.getDateFrom().equals(dateFrom) || flightDTO.getDateFrom().after(dateFrom);
    }

    public static Predicate<FlightDTO> filterFlightsByDestination(String destination){
        return flightDTO -> flightDTO.getDestination().equalsIgnoreCase(destination);
    }

    public static Predicate<FlightDTO> filterFlightsByOrigin(String origin){
        return flightDTO -> flightDTO.getOrigin().equalsIgnoreCase(origin);
    }
}
