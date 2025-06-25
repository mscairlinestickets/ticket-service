package com.erickWck.ticket_service.dto.flight;

import com.erickWck.ticket_service.entity.Flight;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FlightDtoRequest(
        String flightNumber,

        String origin,

        String destination,

        LocalDateTime departureDateTime,

        int totalSeats,

        int availableSeats,

        BigDecimal price,

        String icaoCode,

        String model

) {

    public static Flight dtoToEntity(FlightDtoRequest request) {
        return new Flight(null, request.flightNumber, request.origin, request.destination, request.departureDateTime,
                request.totalSeats, request.availableSeats, request.price, null, null);
    }

}
