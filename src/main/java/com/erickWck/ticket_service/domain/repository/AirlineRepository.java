package com.erickWck.ticket_service.domain.repository;

import com.erickWck.ticket_service.domain.entity.Airline;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {

    Optional<Airline> findByIcaoCode(String iacao);

    @Override
    List<Airline> findAll();

    @Transactional
    void deleteByIcaoCode(String icao);


}
