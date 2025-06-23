package com.erickWck.ticket_service.exception;

public class NoAvailableSeatsException extends RuntimeException {

    public NoAvailableSeatsException(String flightNumber) {
        super("Voo " + flightNumber + " está lotado. Não há assentos disponíveis.");
    }

}
