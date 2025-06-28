package com.erickWck.ticket_service.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();

        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
        return errors;
    }

    @ExceptionHandler(AircraftNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String aircraftNotFoundHandler(AircraftNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AircraftAlreadyException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String aircraftAlreadyHandler(AircraftAlreadyException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String airlineNotFoundHandler(AirlineNotFoundException ex) {
        return ex.getMessage();
    }


    @ExceptionHandler(AirlineAlreadyExist.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String airlineAlreadyExistHandler(AirlineAlreadyExist ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({FlightAlreadyExist.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String lightAlreadyExistHandler(FlightAlreadyExist ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String flightNotFoundHandler(FlightNotFoundException ex) {
        return ex.getMessage();
    }

}
