package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/airlines")
@RestController
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineDtoResponse createNewAirlines(@RequestBody @Valid AirlineDtoRequest airlineRequest) {
        return airlineService.createAirline(airlineRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AirlineDtoResponse> listAllAirlines() {
        return airlineService.findAllAirline();
    }

    @GetMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.OK)
    public AirlineDtoResponse getFindByIdAirlines(@PathVariable @Valid String icaoCode) {
        return airlineService.findByAirline(icaoCode);
    }

    @PutMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.OK)
    public AirlineDtoResponse updateAirlinesExist(@PathVariable @Valid String icaoCode, @RequestBody @Valid AirlineDtoRequest request) {
        return airlineService.editAirline(icaoCode, request);
    }

    @DeleteMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAirlinesExist(@PathVariable @Valid String icaoCode) {
        airlineService.delete(icaoCode);
    }

}

