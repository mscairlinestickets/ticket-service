package com.erickWck.ticket_service.domain.exception;

public class BusinessRuleException extends RuntimeException{

    public BusinessRuleException(String message) {
        super(message);
    }
}
