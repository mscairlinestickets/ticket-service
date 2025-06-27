package com.erickWck.ticket_service.controller;


import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final AircraftService aircraftService;

    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @GetMapping
    public ResponseEntity<List<AircraftDtoResponse>> listAllAircrafts() {
        return ResponseEntity.ok(aircraftService.findAll());
    }

    @GetMapping("/{model}")
    public ResponseEntity<AircraftDtoResponse> getAircraftByModel(@PathVariable String model) {
        return ResponseEntity.ok(aircraftService.findByModelAircraft(model));
    }

    @PostMapping
    public ResponseEntity<AircraftDtoResponse> createNewAircraft(@RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        var created = aircraftService.createAircraft(aircraftRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{model}")
    public ResponseEntity<AircraftDtoResponse> updateAircraft(@PathVariable String model,
                                                              @RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        var updated = aircraftService.editAircraft(model, aircraftRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{model}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAircraft(@PathVariable String model) {
        aircraftService.deleteAircraft(model);
    }


}
