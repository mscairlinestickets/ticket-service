package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.exception.FlightAlreadyExist;
import com.erickWck.ticket_service.domain.exception.FlightNotFoundException;
import com.erickWck.ticket_service.domain.mapper.FlightMapper;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
import com.erickWck.ticket_service.domain.service.flightFunctions.FlightFunctions;
import com.erickWck.ticket_service.domain.service.flightFunctions.FlightValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImplements implements FlightService {

    private final AirlineRepository airLineRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final FlightValidator flightValidator;

    private static final Logger log = LoggerFactory.getLogger(FlightServiceImplements.class);

    public FlightServiceImplements(
            AirlineRepository airLineRepository,
            AircraftRepository aircraftRepository,
            FlightRepository flightRepository,
            FlightValidator flightValidator
    ) {
        this.airLineRepository = airLineRepository;
        this.aircraftRepository = aircraftRepository;
        this.flightRepository = flightRepository;
        this.flightValidator = flightValidator;
    }

    @Override
    public FlightDtoResponse createFlight(FlightDtoRequest request) {
        log.info("Iniciando criação de voo: {}", request.flightNumber());

        var airline = getAirlineOrThrow(request.icaoCode());
        log.debug("Companhia aérea encontrada: {}", airline.getName());

        var aircraft = getAircraftOrThrow(request.model());
        log.debug("Aeronave encontrada: {}", aircraft.getModel());

        Flight flight = FlightMapper.dtoToEntity(request, airline, aircraft);
        flightValidator.validateFlight(flight);
        log.debug("Validação do voo concluída com sucesso.");

        var existFly = flightRepository.findByFlightNumber(request.flightNumber());

        var result = flightRepository.save(flight);
        log.info("Voo registrado com sucesso: {} (ID: {})", result.getFlightNumber(), result.getId());

        return FlightMapper.entityToDto(result);
    }

    @Override
    public FlightDtoResponse findFlightNumber(String flyNumber) {
        log.info("Buscando voo pelo número: {}", flyNumber);
        var flight = flightRepository.findByFlightNumber(flyNumber)
                .orElseThrow(() -> {
                    log.error("Voo não encontrado: {}", flyNumber);
                    return new FlightNotFoundException(flyNumber);
                });

        log.debug("Voo encontrado: {}", flight.getFlightNumber());
        return FlightMapper.entityToDto(flight);
    }

    @Override
    public List<FlightDtoResponse> findAllFlight() {
        log.info("Buscando todos os voos cadastrados.");
        var flights = flightRepository.findAll();
        log.debug("Total de voos encontrados: {}", flights.size());

        return flights.stream()
                .map(FlightMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FlightDtoResponse editFlight(String flyNumber, FlightDtoRequest request) {
        log.info("Editando voo: {}", flyNumber);
        return flightRepository.findByFlightNumber(flyNumber)
                .map(existFly -> {
                    log.debug("Voo encontrado para edição: {}", existFly.getFlightNumber());

                    Flight flightUpdate = Flight.builder()
                            .id(existFly.getId())
                            .flightNumber(existFly.getFlightNumber())
                            .origin(request.origin())
                            .destination(request.destination())
                            .departureDateTime(request.departureDateTime())
                            .totalSeats(request.totalSeats())
                            .availableSeats(request.availableSeats())
                            .price(request.price())
                            .airline(existFly.getAirline())
                            .aircraft(existFly.getAircraft())
                            .createdDate(existFly.getCreatedDate())
                            .lastModifiedDate(existFly.getLastModifiedDate())
                            .version(existFly.getVersion())
                            .build();

                    var updated = flightRepository.save(flightUpdate);
                    log.info("Voo atualizado com sucesso: {} (ID: {})", updated.getFlightNumber(), updated.getId());
                    return FlightMapper.entityToDto(updated);
                })
                .orElseThrow(() -> {
                    log.error("Voo para edição não encontrado: {}", flyNumber);
                    return new FlightNotFoundException(flyNumber);
                });
    }

    @Override
    public void delete(String flyNumber) {
        log.info("Iniciando remoção do voo: {}", flyNumber);
        var flight = findFlightNumber(flyNumber); // já faz log internamente
        flightRepository.deleteByFlightNumber(flight.flightNumber());
        log.info("Voo removido com sucesso: {}", flight.flightNumber());
    }

    @Override
    public FlightDtoResponse orderBookingFlight(String flightNumber, int quantity) {
        log.info("Iniciando orderBookingFlight para o voo [{}] com quantidade [{}]", flightNumber, quantity);

        var flightOrderBooking = flightRepository.findByFlightNumber(flightNumber).get();

        log.debug("Antes da reserva: assentos disponíveis [{}]", flightOrderBooking.getAvailableSeats());
        FlightFunctions.decrementAvailableSeats(flightOrderBooking, quantity);

        log.debug("Depois da reserva: assentos disponíveis [{}]", flightOrderBooking.getAvailableSeats());
        flightRepository.save(flightOrderBooking);

        log.info("Reserva finalizada com sucesso para o voo [{}]", flightNumber);
        return FlightMapper.entityToDto(flightOrderBooking);
    }

    private Airline getAirlineOrThrow(String icaoCode) {
        log.debug("Buscando companhia aérea pelo ICAO: {}", icaoCode);
        return airLineRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> {
                    log.error("Companhia aérea não encontrada: {}", icaoCode);
                    return new AirlineNotFoundException(icaoCode);
                });
    }

    private Aircraft getAircraftOrThrow(String model) {
        log.debug("Buscando aeronave pelo modelo: {}", model);
        return aircraftRepository.findByModel(model)
                .orElseThrow(() -> {
                    log.error("Aeronave não encontrada: {}", model);
                    return new AircraftNotFoundException(model);
                });
    }
}
