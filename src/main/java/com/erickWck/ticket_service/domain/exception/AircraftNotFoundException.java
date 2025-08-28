package com.erickWck.ticket_service.domain.exception;

public class AircraftNotFoundException extends RuntimeException {

    public AircraftNotFoundException(String model) {
        super("Aircraft with model: " + model + " not found.");
    }


}
