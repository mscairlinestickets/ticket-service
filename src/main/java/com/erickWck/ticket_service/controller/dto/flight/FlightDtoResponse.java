package com.erickWck.ticket_service.controller.dto.flight;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FlightDtoResponse(

        String flightNumber,

        String origin,

        String destination,

        LocalDateTime departureDateTime,

        int totalSeats,

        int availableSeats,

        BigDecimal price,

        String airlineName,

        String icaoCode,

        String aircraftModel,

        int aircraftCapacity
) {


}
