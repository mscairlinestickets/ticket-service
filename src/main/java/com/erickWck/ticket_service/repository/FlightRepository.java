package com.erickWck.ticket_service.repository;

import com.erickWck.ticket_service.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    boolean existsByFlightNumberAndDepartureDateTimeAndDestinationNot(String flightNumber, LocalDateTime departureDateTime, String destination);

    boolean existsByFlightNumberAndDepartureDateTime(String flightNumber, LocalDateTime departureDateTime);
}
