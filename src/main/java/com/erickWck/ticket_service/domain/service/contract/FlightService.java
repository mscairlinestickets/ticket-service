package com.erickWck.ticket_service.domain.service.contract;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;

import java.util.List;

public interface FlightService {

    FlightDtoResponse createFlight(FlightDtoRequest request);
    FlightDtoResponse findFlightNumber(String flyNumber);
    List<FlightDtoResponse> findAllFlight();
    FlightDtoResponse editFlight(String flyNumber, FlightDtoRequest request);
    void delete(String flyBumber);
    FlightDtoResponse orderBookingFlight (String flightNumber, int quantity);

}
