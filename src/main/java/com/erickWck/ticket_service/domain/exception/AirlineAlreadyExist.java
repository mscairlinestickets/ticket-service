package com.erickWck.ticket_service.domain.exception;

public class AirlineAlreadyExist extends RuntimeException {

    public AirlineAlreadyExist(String icao) {
        super("Airline with icao code: " + icao + " already exist.");
    }

}
