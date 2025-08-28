package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.exception.AircraftAlreadyException;
import com.erickWck.ticket_service.domain.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.domain.mapper.AircraftMapper;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AircraftServiceImplements implements AircraftService {

    private final AircraftRepository aircraftRepository;

    public AircraftServiceImplements(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    @Override
    public AircraftDtoResponse createAircraft(AircraftDtoRequest request) {
        var aircraft = aircraftRepository.findByModel(request.model());

        aircraftRepository.findByModel(request.model())
                .ifPresent(exist -> {
                    log.warn("Falha ao criar aeronave: modelo '{}' já existente.", request.model());
                    throw new AircraftAlreadyException(request.model());
                });

        var aircraftSave = aircraftRepository.save(AircraftMapper.createAircraft(request));
        log.info("Aeronave criada com sucesso: {}", aircraftSave);
        return AircraftMapper.entityToDto(aircraftSave);
    }

    @Override
    public AircraftDtoResponse findByModelAircraft(String model) {
        var aircraft = aircraftRepository.findByModel(model)
                .orElseThrow(() -> {
                    log.warn("Aeronave nao encontrada para o modelo: '{}'", model);
                    return new AircraftNotFoundException(model);
                });

        log.info("Aeronave localizada: {}", aircraft);
        return AircraftMapper.entityToDto(aircraft);
    }

    @Override
    public List<AircraftDtoResponse> findAll() {
        var list = aircraftRepository.findAll();
        log.info("Listagem de aeronaves: {} registro(s) encontrado(s).", list.size());
        return list.stream().map(AircraftMapper::entityToDto).collect(Collectors.toList());
    }

    @Override
    public AircraftDtoResponse editAircraft(String model, AircraftDtoRequest request) {
        return aircraftRepository.findByModel(model)
                .map(existAir -> {
                    var aircraft = Aircraft.builder()
                            .uuid(existAir.getUuid())
                            .model(request.model())
                            .manufacturer(request.manufacturer())
                            .seatCapacity(request.seatCapacity())
                            .createdDate(existAir.getCreatedDate())
                            .lastModifiedDate(existAir.getLastModifiedDate())
                            .version(existAir.getVersion())
                            .build();
                    aircraftRepository.save(aircraft);
                    log.info("Aeronave editada com sucesso: {}", aircraft);
                    return AircraftMapper.entityToDto(aircraft);
                })
                .orElseThrow(() -> {
                    log.warn("Falha na edição: aeronave com modelo '{}' não encontrada.", model);
                    return new AircraftNotFoundException(model);
                });
    }

    @Override
    public void deleteAircraft(String model) {
        var modelFound = findByModelAircraft(model).model(); // já loga dentro
        aircraftRepository.deleteByModel(modelFound);
        log.info("Aeronave excluída: modelo '{}'", modelFound);
    }
}
