package com.erickWck.ticket_service.domain.repository;

import com.erickWck.ticket_service.domain.entity.Aircraft;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AircraftRepository extends CrudRepository<Aircraft, Long> {
    Optional<Aircraft> findByModel(String model);
}
