package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/flights")
@RestController
public class FlightController {


    private FlightService flightService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightDtoResponse createNewFlight(@RequestBody @Valid FlightDtoRequest flightRequest) {
        return flightService.createFlight(flightRequest);
    }

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FlightDtoResponse> listAllFlights() {
        return flightService.findAllFlight();
    }

    @GetMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.OK)
    public FlightDtoResponse findFlightDetails(@PathVariable @Valid String flightNumber) {
        return flightService.findFlightNumber(flightNumber);
    }

    @PutMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.OK)
    public FlightDtoResponse updateFlightsExist(@PathVariable @Valid String flightNumber,
                                                @RequestBody @Valid FlightDtoRequest request) {
        return flightService.editFlight(flightNumber, request);
    }

    @DeleteMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlightsExist(@PathVariable @Valid String flightNumber) {
        flightService.delete(flightNumber);
    }

}
