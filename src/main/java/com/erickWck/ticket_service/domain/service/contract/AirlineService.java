package com.erickWck.ticket_service.domain.service.contract;

import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.dto.airline.AirlineDtoResponse;

import java.util.List;

public interface AirlineService {

    AirlineDtoResponse createAirline(AirlineDtoRequest request);
    AirlineDtoResponse findByAirline(String icaoCode);
    List<AirlineDtoResponse> findAllAirline();
    AirlineDtoResponse editAirline(String icaoCode, AirlineDtoRequest request);
    void delete(String icaoCode);

}
