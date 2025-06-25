package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.dto.flight.FlightDtoRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ticket")
@RestController
public class FlightController {

    @GetMapping("/api/flights")
    public List<FlightDtoResponse> listAllFlights() {
        return null;
    }

    @GetMapping("/api/flights/{id}")
    public FlightDtoResponse getFindByIdFlights(long id) {
        return null;
    }

    @PostMapping("/api/flights")
    public FlightDtoResponse createNewFlight(@RequestBody @Valid FlightDtoRequest flightRequest) {
        return null;
    }

    @PutMapping("/api/flights/{id}")
    public FlightDtoResponse updateFlightsExist(@PathVariable long id) {
        return null;
    }

    @DeleteMapping("/api/flights/{id}")
    public void deleteFlightsExist(@PathVariable long id) {

    }

}
