package com.erickWck.ticket_service.service;

import com.erickWck.ticket_service.dto.FlightDtoRequest;
import com.erickWck.ticket_service.dto.FlightDtoResponse;
import com.erickWck.ticket_service.entity.Aircraft;
import com.erickWck.ticket_service.entity.Airline;
import com.erickWck.ticket_service.entity.Flight;
import com.erickWck.ticket_service.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.mapper.FlightMapper;
import com.erickWck.ticket_service.repository.AirLineRepository;
import com.erickWck.ticket_service.repository.AircraftRepository;
import com.erickWck.ticket_service.repository.FlightRepository;
import com.erickWck.ticket_service.service.contract.FlightService;
import com.erickWck.ticket_service.service.flight.FlightValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImplements implements FlightService {


    private final AirLineRepository airLineRepository;
    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final FlightValidator flightValidator;
    private static final Logger log = LoggerFactory.getLogger(FlightServiceImplements.class);

    public FlightServiceImplements(AirLineRepository airLineRepository, AircraftRepository aircraftRepository,
                                   FlightRepository flightRepository, FlightValidator flightValidator) {
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

        var result = flightRepository.save(flight);

        log.info("Voo registrado com sucesso: {} (ID: {})", result.getFlightNumber(), result.getId());
        return FlightMapper.entityToDto(result);
    }


    @Override
    public FlightDtoResponse findById(Long id) {
        return null;
    }

    @Override
    public List<FlightDtoResponse> findAll() {
        return List.of();
    }

    @Override
    public FlightDtoResponse update(Long id, FlightDtoRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

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
