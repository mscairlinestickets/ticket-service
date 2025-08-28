package com.erickWck.ticket_service.controller.dto.aircraft;

public record AircraftDtoResponse(

        String model,
        String manufacturer,
        int seatCapacity
) {
}
