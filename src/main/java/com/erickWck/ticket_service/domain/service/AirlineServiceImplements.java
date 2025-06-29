package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.exception.AirlineAlreadyExist;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.mapper.AirlineMapper;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.service.contract.AirlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AirlineServiceImplements implements AirlineService {

    private final AirlineRepository airlineRepository;

    public AirlineServiceImplements(AirlineRepository airlineRepository) {
        this.airlineRepository = airlineRepository;
    }

    @Override
    public AirlineDtoResponse createAirline(AirlineDtoRequest request) {
        var existAir = airlineRepository.findByIcaoCode(request.icaoCode());
        if (existAir.isPresent()) {
            log.warn("Falha ao criar companhia aérea: ICAO {} já existente.", request.icaoCode());
            throw new AirlineAlreadyExist(request.icaoCode());
        }

        var airline = airlineRepository.save(AirlineMapper.createAirline(request));
        log.info("Companhia aérea criada com sucesso: {}", airline);
        return AirlineMapper.entityToDto(airline);
    }

    @Override
    public AirlineDtoResponse findByAirline(String icao) {
        var airline = airlineRepository.findByIcaoCode(icao)
                .orElseThrow(() -> {
                    log.warn("Companhia aérea não encontrada: ICAO {}", icao);
                    return new AirlineNotFoundException(icao);
                });

        log.info("Companhia aérea localizada: {}", airline);
        return AirlineMapper.entityToDto(airline);
    }

    @Override
    public List<AirlineDtoResponse> findAllAirline() {
        var list = airlineRepository.findAll();
        log.info("Listagem de companhias aéreas: {} registro(s) encontrado(s).", list.size());
        return list.stream()
                .map(AirlineMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AirlineDtoResponse editAirline(String icaoCode, AirlineDtoRequest request) {
        return airlineRepository.findByIcaoCode(icaoCode)
                .map(airExist -> {
                    var airline = Airline.builder()
                            .id(airExist.getId())
                            .name(request.name())
                            .icaoCode(airExist.getIcaoCode())
                            .createdDate(airExist.getCreatedDate())
                            .lastModifiedDate(airExist.getLastModifiedDate())
                            .version(airExist.getVersion())
                            .build();
                    airlineRepository.save(airline);
                    log.info("Companhia aérea editada com sucesso: {}", airline);
                    return AirlineMapper.entityToDto(airline);
                }).orElseThrow(() -> {
                    log.warn("Falha na edição: companhia aérea com ICAO {} não encontrada.", icaoCode);
                    return new AirlineNotFoundException(icaoCode);
                });
    }

    @Override
    public void delete(String iacao) {
        var icaoCode = findByAirline(iacao).icaoCode(); // já loga se não existir
        airlineRepository.deleteByIcaoCode(icaoCode);
        log.info("Companhia aérea excluída: ICAO {}", icaoCode);
    }
}
