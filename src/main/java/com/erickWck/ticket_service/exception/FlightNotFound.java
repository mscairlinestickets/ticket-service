package com.erickWck.ticket_service.exception;

public class FlightNotFound extends RuntimeException {

    public  FlightNotFound(String flyNumber){
        super("Flight with fly number: " + flyNumber + " not found.");
    }
}
