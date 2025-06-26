package com.erickWck.ticket_service.domain.mapper;

import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.entity.Airline;

public class Airlinemapper {

    public static AirlineDtoResponse entityToDto(Airline airline) {
        return new AirlineDtoResponse(airline.name(), airline.icaoCode());
    }

    public static Airline createAirline(AirlineDtoRequest request) {
        return new Airline(null, request.name(), request.iacaoCode());
    }
}