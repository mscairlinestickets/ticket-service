package com.erickWck.ticket_service.controller;


import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftDtoResponse createNewAircraft(@RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return aircraftService.createAircraft(aircraftRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AircraftDtoResponse> listAllAircrafts() {
        return aircraftService.findAll();
    }

    @GetMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftDtoResponse findAircraftDetails(@PathVariable @Valid String model) {
        return aircraftService.findByModelAircraft(model);
    }

    @PutMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftDtoResponse editAircraft(@PathVariable String model,
                                              @RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return aircraftService.editAircraft(model, aircraftRequest);
    }

    @DeleteMapping("/{model}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAircraft(@PathVariable @Valid String model) {
        aircraftService.deleteAircraft(model);
    }



}

