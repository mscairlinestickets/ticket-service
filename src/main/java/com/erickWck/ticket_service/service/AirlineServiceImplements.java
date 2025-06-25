package com.erickWck.ticket_service.service;

import com.erickWck.ticket_service.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.entity.Airline;
import com.erickWck.ticket_service.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.exception.FlightNotFound;
import com.erickWck.ticket_service.mapper.Airlinemapper;
import com.erickWck.ticket_service.mapper.FlightMapper;
import com.erickWck.ticket_service.repository.AirlineRepository;
import com.erickWck.ticket_service.service.contract.AirlineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AirlineServiceImplements implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineServiceImplements(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public AirlineDtoResponse createAirline(AirlineDtoResponse request) {
        return null;
    }

    @Override
    public AirlineDtoResponse findByAirline(String icao) {

        return Airlinemapper.entityToDto(airlineRepository.findByIcaoCode(icao)
                .orElseThrow(() -> new AirlineNotFoundException(icao))
        );

    }

    @Override
    public List<AirlineDtoResponse> findAllAirline() {
        return airlineRepository.findAll()
                .stream()
                .map(Airlinemapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AirlineDtoResponse editAirline(String icaoCode, AirlineDtoRequest request) {

        return airlineRepository.findByIcaoCode(icaoCode)
                .map(airExist -> {
                    var airline = Airline.builder()
                            .id(airExist.id())
                            .name(request.name())
                            .icaoCode(airExist.icaoCode())
                            .build();
                    airlineRepository.save(airline);
                    return Airlinemapper.entityToDto(airline);
                }).orElseThrow(() -> new AirlineNotFoundException(icaoCode));
    }

    @Override
    public void delete(String iacao) {
        airlineRepository.deleteByIcaoCode(findByAirline(iacao).icaoCode());
    }


}
