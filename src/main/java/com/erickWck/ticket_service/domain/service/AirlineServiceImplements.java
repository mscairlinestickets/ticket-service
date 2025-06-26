package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.exception.AirlineAlreadyExist;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.mapper.Airlinemapper;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.service.contract.AirlineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirlineServiceImplements implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineServiceImplements(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public AirlineDtoResponse createAirline(AirlineDtoRequest request) {

        var existAir = airlineRepository.findByIcaoCode(request.iacaoCode());
        if (existAir.isPresent()) {
            throw new AirlineAlreadyExist(request.iacaoCode());
        }

        var airline = airlineRepository.save(Airlinemapper.createAirline(request));
        return Airlinemapper.entityToDto(airline);
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
