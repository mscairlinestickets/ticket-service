package com.erickWck.ticket_service.demo;

import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@Profile("test")
public class TicketDemoDataLoader {

    private final FlightRepository flightRepository;
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    public TicketDemoDataLoader(FlightRepository flightRepository, AircraftRepository aircraftRepository, AirlineRepository airlineRepository) {
        this.flightRepository = flightRepository;
        this.aircraftRepository = aircraftRepository;
        this.airlineRepository = airlineRepository;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void loadTicketTestData() {

        log.info("🔁 Limpando dados antigos para perfil 'test'");
        flightRepository.deleteAll();
        aircraftRepository.deleteAll();
        airlineRepository.deleteAll();

        var airline = new Airline(null, "LATAM", "TAM", Instant.now(), Instant.now(), 0);
        var aircraft = new Aircraft(null, "A320", "Airbus", 180, Instant.now(), Instant.now(), 0);

        airline = airlineRepository.save(airline);
        aircraft = aircraftRepository.save(aircraft);

        var flight = new Flight(null, "AZU4321", "GRU", "REC",
                LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                180, 180, new BigDecimal("899.99"), airline, aircraft,
                Instant.now(), Instant.now(), null, null,0);

        flightRepository.save(flight);
        log.info("Dados de teste salvos com sucesso.");
    }


}
