package com.erickWck.ticket_service.service.contract;

import com.erickWck.ticket_service.dto.FlightDtoRequest;
import com.erickWck.ticket_service.dto.FlightDtoResponse;

import java.util.List;

public interface FlightService {

    FlightDtoResponse createFlight(FlightDtoRequest request);
    FlightDtoResponse findFlightNumber(String flyNumber);
    List<FlightDtoResponse> findAllFlight();
    FlightDtoResponse editFlight(String flyNumber, FlightDtoRequest request);
    void delete(String flyBumber);

}
