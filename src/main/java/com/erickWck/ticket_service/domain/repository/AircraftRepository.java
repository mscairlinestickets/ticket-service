package com.erickWck.ticket_service.domain.repository;

import com.erickWck.ticket_service.domain.entity.Aircraft;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    Optional<Aircraft> findByModel(String model);

    List<Aircraft> findAll();

    @Transactional
    void deleteByModel(String model);

}
