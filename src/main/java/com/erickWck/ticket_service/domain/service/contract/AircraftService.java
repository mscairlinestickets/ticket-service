package com.erickWck.ticket_service.domain.service.contract;

import com.erickWck.ticket_service.domain.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.domain.dto.aircraft.AircraftDtoResponse;

import java.util.List;

public interface AircraftService {

    AircraftDtoResponse createAircraft(AircraftDtoRequest request);
    AircraftDtoResponse findById(long id);
    List<AircraftDtoResponse> findAll();
    AircraftDtoResponse update(long id, AircraftDtoRequest request);
    void delete(long id);

}