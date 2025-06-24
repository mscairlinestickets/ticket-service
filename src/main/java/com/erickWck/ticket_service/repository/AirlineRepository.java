package com.erickWck.ticket_service.repository;

import com.erickWck.ticket_service.entity.Airline;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AirlineRepository extends CrudRepository<Airline, Long> {

    Optional<Airline> findByIcaoCode(String tam);

}
