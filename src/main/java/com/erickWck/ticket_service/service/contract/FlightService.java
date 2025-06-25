package com.erickWck.ticket_service.service.contract;

import com.erickWck.ticket_service.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.dto.flight.FlightDtoResponse;

import java.util.List;

public interface FlightService {

    FlightDtoResponse createFlight(FlightDtoRequest request);
    FlightDtoResponse findFlightNumber(String flyNumber);
    List<FlightDtoResponse> findAllFlight();
    FlightDtoResponse editFlight(String flyNumber, FlightDtoRequest request);
    void delete(String flyBumber);

}
