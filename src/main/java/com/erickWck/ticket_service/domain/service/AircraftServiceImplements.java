package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AircraftServiceImplements implements AircraftService {

    private final AircraftRepository aircraftRepository;

    public AircraftServiceImplements(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public AircraftDtoResponse createAircraft(AircraftDtoRequest request) {
        return null;
    }

    @Override
    public AircraftDtoResponse findById(long id) {
        return null;
    }

    @Override
    public List<Aircraft> findAll() {
        return aircraftRepository.findAll();
    }

    @Override
    public AircraftDtoResponse update(long id, AircraftDtoRequest request) {
        return null;
    }

    @Override
    public void delete(long id) {

    }
}
