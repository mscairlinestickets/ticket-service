package com.erickWck.ticket_service.domain.mapper;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;

public class FlightMapper {

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