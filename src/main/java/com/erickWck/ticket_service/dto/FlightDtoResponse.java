package com.erickWck.ticket_service.dto;

import com.erickWck.ticket_service.entity.Flight;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FlightDtoResponse(

        String flightNumber,

        String origin,

        String destination,

        LocalDateTime departureDateTime,

        int totalSeats,

        int availableSeats,

        String airlineName,

        String icaoCode,

        String aircraftModel,

        int aircraftCapacity
) {

    public static FlightDtoResponse entityToDto(Flight flight) {
        return new FlightDtoResponse(flight.getFlightNumber(), flight.getOrigin(), flight.getDestination(), flight.getDepartureDateTime(),
                flight.getTotalSeats(), flight.getAvailableSeats(), flight.getAirline().name(),
                flight.getAirline().icaoCode(), flight.getAircraft().model(), flight.getAircraft().seatCapacity());
    }

}
