package com.erickWck.ticket_service.domain.repository;

import com.erickWck.ticket_service.domain.entity.Flight;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumberAndDepartureDateTimeAndDestinationNot(String flightNumber, LocalDateTime departureDateTime, String destination);

    boolean existsByFlightNumberAndDepartureDateTime(String flightNumber, LocalDateTime departureDateTime);

    Optional<Flight> findByFlightNumber(String flyNumber);

    List<Flight> findAll();

    @Transactional
    void deleteByFlightNumber(String flyNumber);

}
