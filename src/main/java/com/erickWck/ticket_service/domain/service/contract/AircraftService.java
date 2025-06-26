package com.erickWck.ticket_service.domain.service.contract;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;

import java.util.List;

public interface AircraftService {

    AircraftDtoResponse createAircraft(AircraftDtoRequest request);
    AircraftDtoResponse findById(long id);
    List<Aircraft> findAll();
    AircraftDtoResponse update(long id, AircraftDtoRequest request);
    void delete(long id);

}