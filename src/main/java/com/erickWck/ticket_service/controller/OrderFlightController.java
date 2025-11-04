package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.BookingRequestPayload;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class OrderFlightController {

    private final FlightService flightService;

    public OrderFlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/orderBooking")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FlightDtoResponse orderFlight(@Valid @RequestBody BookingRequestPayload request) {
        return flightService.orderBookingFlight(request.flightNumber(), request.quantity());
    }

}
