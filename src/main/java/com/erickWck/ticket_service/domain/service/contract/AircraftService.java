package com.erickWck.ticket_service.domain.service.contract;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;

import java.util.List;

public interface AircraftService {

    AircraftDtoResponse createAircraft(AircraftDtoRequest request);

    AircraftDtoResponse findByModelAircraft(String model);

    List<AircraftDtoResponse> findAll();

    AircraftDtoResponse editAircraft(String model, AircraftDtoRequest request);

    void deleteAircraft(String model);

}