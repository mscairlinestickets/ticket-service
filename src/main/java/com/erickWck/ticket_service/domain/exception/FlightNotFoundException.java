package com.erickWck.ticket_service.domain.exception;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(String flyNumber){
        super("Flight with fly number: " + flyNumber + " not found.");
    }
}
