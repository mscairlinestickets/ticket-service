package com.erickWck.ticket_service.mapper;

import com.erickWck.ticket_service.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.entity.Airline;

public class Airlinemapper {

    public static AirlineDtoResponse entityToDto(Airline airline) {
        return new AirlineDtoResponse(airline.name(), airline.icaoCode());
    }

}