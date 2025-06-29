package com.erickWck.ticket_service.domain.mapper;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;

public class FlightMapper {

    public static Flight dtoToEntity(FlightDtoRequest request, Airline airline, Aircraft aircraft) {
        return new Flight(
                null,
                request.flightNumber(),
                request.origin(),
                request.destination(),
                request.departureDateTime(),
                request.totalSeats(),
                request.availableSeats(),
                request.price(),
                airline,
                aircraft, null, null, 0
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
                flight.getAirline().getName(),
                flight.getAirline().getIcaoCode(),
                flight.getAircraft().getModel(),
                flight.getAircraft().getSeatCapacity()
        );
    }
}