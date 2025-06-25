package com.erickWck.ticket_service.service.contract;

import com.erickWck.ticket_service.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.dto.airline.AirlineDtoResponse;

import java.util.List;

public interface AirlineService {

    AirlineDtoResponse createAirline(AirlineDtoResponse request);
    AirlineDtoResponse findByAirline(String icaoCode);
    List<AirlineDtoResponse> findAllAirline();
    AirlineDtoResponse editAirline(String icaoCode, AirlineDtoRequest request);
    void delete(String icaoCode);

}
