package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.domain.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.exception.FlightAlreadyExist;
import com.erickWck.ticket_service.domain.exception.FlightNotFound;
import com.erickWck.ticket_service.domain.mapper.FlightMapper;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
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


    public FlightServiceImplements(AirlineRepository airLineRepository, AircraftRepository aircraftRepository, FlightRepository flightRepository, FlightValidator flightValidator) {
        this.airLineRepository = airLineRepository;
        this.aircraftRepository = aircraftRepository;
        this.flightRepository = flightRepository;
        this.flightValidator = flightValidator;
    }

    @Override
    public FlightDtoResponse createFlight(FlightDtoRequest request) {

        log.info("Iniciando criação de voo: {}", request.flightNumber());
        var airline = getAirlineOrThrow(request.icaoCode());
        var aircraft = getAircraftOrThrow(request.model());

        Flight flight = FlightMapper.dtoToEntity(request, airline, aircraft);

        flightValidator.validateFlight(flight);

        var existFly = flightRepository.findByFlightNumber(request.flightNumber());

        if (existFly.isPresent()){
            throw new FlightAlreadyExist(request.flightNumber());
        }

        var result = flightRepository.save(flight);

        log.info("Voo registrado com sucesso: {} (ID: {})", result.getFlightNumber(), result.getId());
        return FlightMapper.entityToDto(result);
    }


    @Override
    public FlightDtoResponse findFlightNumber(String flyNumber) {

        return FlightMapper.entityToDto(flightRepository.findByFlightNumber(flyNumber)
                .orElseThrow(() -> new FlightNotFound(flyNumber))
        );
    }

    @Override
    public List<FlightDtoResponse> findAllFlight() {
        return flightRepository.findAll()
                .stream().map(FlightMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FlightDtoResponse editFlight(String flyNumber, FlightDtoRequest request) {

        return flightRepository.findByFlightNumber(flyNumber)
                .map(existFly -> {
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
                            .build();
                    flightRepository.save(flightUpdate);
                    return FlightMapper.entityToDto(flightUpdate);
                })
                .orElseThrow(() -> new FlightNotFound(flyNumber));
    }

    @Override
    public void delete(String flyNumber) {

        flightRepository.deleteByFlightNumber(findFlightNumber(flyNumber).flightNumber());

    }

    private Airline getAirlineOrThrow(String icaoCode) {
        return airLineRepository.findByIcaoCode(icaoCode)
                .orElseThrow(() -> new AirlineNotFoundException(icaoCode));
    }

    private Aircraft getAircraftOrThrow(String model) {
        return aircraftRepository.findByModel(model)
                .orElseThrow(() -> new AircraftNotFoundException(model));
    }

     /*processar reserva
    public FlightDtoResponse createFlight(FlightDtoRequest request) {

        log.info("Iniciando criação de voo: {}", request.flightNumber());
        var airline = getAirlineOrThrow(request.icaoCode());
        var aircraft = getAircraftOrThrow(request.model());

        Flight flight = FlightMapper.dtoToEntity(request, airline, aircraft);

        FlightFunctions.decrementAvailableSeats(flight);
        log.info("Assento reservado com sucesso para o voo {}", flight.getAvailableSeats());
        log.info("Assento reservado com sucesso para o voo {}", flight.getFlightNumber());
        var result = flightRepository.save(flight);

        log.info("Assento reservado com sucesso para o voo {}", flight.getAvailableSeats());
        log.info("Voo {} salvo com ID: {}", result.getFlightNumber(), result.getId());
        return FlightMapper.entityToDto(result);
    }
    */

}
