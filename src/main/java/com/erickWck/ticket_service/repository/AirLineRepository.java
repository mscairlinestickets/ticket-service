package com.erickWck.ticket_service.repository;

import com.erickWck.ticket_service.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AirLineRepository  extends JpaRepository <Airline, Long> {

    Optional<Airline> findByIcaoCode(String tam);

}
