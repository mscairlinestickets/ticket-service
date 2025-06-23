package com.erickWck.ticket_service.exception;

public class AirlineNotFoundException extends RuntimeException {

    public AirlineNotFoundException(String icaoCode) {
        super("Airline with ICAO code " + icaoCode + " not found.");
    }

}
