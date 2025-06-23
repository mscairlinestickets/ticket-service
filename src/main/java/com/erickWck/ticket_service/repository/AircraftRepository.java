package com.erickWck.ticket_service.repository;

import com.erickWck.ticket_service.entity.Aircraft;

import java.util.Optional;

public interface AircraftRepository {
    Optional<Aircraft> findByModel(String model);
}
