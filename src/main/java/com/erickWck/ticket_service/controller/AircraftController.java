package com.erickWck.ticket_service.controller;


import com.erickWck.ticket_service.domain.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.domain.dto.aircraft.AircraftDtoResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/aircraft")
@RestController
public class AircraftController {

    @GetMapping("/api/aircrafts")
    public List<AircraftDtoResponse> listAllAircrafts() {
        return null;
    }

    @GetMapping("/api/aircrafts/{id}")
    public AircraftDtoResponse getAircraftById(@PathVariable long id) {
        return null;
    }

    @PostMapping("/api/aircrafts")
    public AircraftDtoResponse createNewAircraft(@RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return null;
    }

    @PutMapping("/api/aircrafts/{id}")
    public AircraftDtoResponse updateAircraftExist(@PathVariable long id,
                                                   @RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return null;
    }

    @DeleteMapping("/api/aircrafts/{id}")
    public void deleteAircraftExist(@PathVariable long id) {
    }


}
