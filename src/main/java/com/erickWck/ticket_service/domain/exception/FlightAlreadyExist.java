package com.erickWck.ticket_service.domain.exception;

public class FlightAlreadyExist extends RuntimeException {

    public FlightAlreadyExist(String flyNumber) {
        super("Flight with number: " + flyNumber + " already exist.");
    }

}
