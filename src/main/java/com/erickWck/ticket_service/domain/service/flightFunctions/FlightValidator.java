package com.erickWck.ticket_service.domain.service.flightFunctions;

import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.BusinessRuleException;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FlightValidator {

    private final FlightRepository flightRepository;

    public FlightValidator(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void validateFlight(Flight flight) {
        validateDepartureIsFuture(flight);
        validateDuplicateWithDifferentDestination(flight);
        validateExactDuplicate(flight);
    }

    private void validateDepartureIsFuture(Flight flight) {
        if (flight.getDepartureDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("A data de partida deve ser no futuro.");
        }
    }

    private void validateDuplicateWithDifferentDestination(Flight flight) {
        if (flightRepository.existsByFlightNumberAndDepartureDateTimeAndDestinationNot
                (flight.getFlightNumber(), flight.getDepartureDateTime(), flight.getDestination())) {
            throw new BusinessRuleException("Já existe um voo com esse número e horário para outro destino.");
        }
    }

    private void validateExactDuplicate(Flight flight) {
        if (flightRepository.existsByFlightNumberAndDepartureDateTime(
                flight.getFlightNumber(), flight.getDepartureDateTime())) {
            throw new BusinessRuleException("Voo já cadastrado com esse número e horário.");
        }
    }
}
