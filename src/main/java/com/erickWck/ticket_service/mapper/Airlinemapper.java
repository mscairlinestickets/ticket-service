package com.erickWck.ticket_service.mapper;

import com.erickWck.ticket_service.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.entity.Aircraft;
import com.erickWck.ticket_service.entity.Airline;
import com.erickWck.ticket_service.entity.Flight;

public class Airlinemapper {

    public static Flight dtoToEntity(FlightDtoRequest dto, Airline airline, Aircraft aircraft) {
        return new Flight(
                null,
                dto.flightNumber(),
                dto.origin(),
                dto.destination(),
                dto.departureDateTime(),
                dto.totalSeats(),
                dto.availableSeats(),
                dto.price(),
                airline,
                aircraft
        );
    }

    public static FlightDtoResponse entityToDto(Flight flight) {
        return new FlightDtoResponse(
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureDateTime(),
                flight.getTotalSeats(),
                flight.getAvailableSeats(),
                flight.getPrice(),
                flight.getAirline().name(),
                flight.getAirline().icaoCode(),
                flight.getAircraft().model(),
                flight.getAircraft().seatCapacity()
        );
    }
}