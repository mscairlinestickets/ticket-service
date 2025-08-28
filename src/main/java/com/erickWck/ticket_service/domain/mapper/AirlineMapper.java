package com.erickWck.ticket_service.domain.mapper;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.entity.Airline;

public class AirlineMapper {

    public static AirlineDtoResponse entityToDto(Airline airline) {
        return new AirlineDtoResponse(airline.getName(), airline.getIcaoCode());
    }

    public static Airline createAirline(AirlineDtoRequest request) {
        return new Airline(null, request.name(), request.icaoCode(),null,null,0);
    }
}