package com.erickWck.ticket_service.domain.repository;

import com.erickWck.ticket_service.domain.entity.Aircraft;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends CrudRepository<Aircraft, Long> {

    Optional<Aircraft> findByModel(String model);

    List<Aircraft> findAll();

    void deleteByModel(String model);

}
