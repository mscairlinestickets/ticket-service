package com.erickWck.ticket_service.controller;


import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Gestão de Aeronaves", description = "API responsável por realizar e administrar a gestão de aeronaves.")
@RestController
@RequestMapping("/api/aircrafts")
public class AircraftController {

    private final AircraftService aircraftService;
    public AircraftController(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @Operation(summary = "Cria uma nova aeronave")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aeronave criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftDtoResponse createNewAircraft(@RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return aircraftService.createAircraft(aircraftRequest);
    }

    @Operation(summary = "Lista todas as aeronaves")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AircraftDtoResponse> listAllAircrafts() {
        return aircraftService.findAll();
    }

    @Operation(summary = "Consulta detalhes de uma aeronave pelo modelo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeronave encontrada"),
            @ApiResponse(responseCode = "404", description = "Aeronave não encontrada")
    })
    @GetMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftDtoResponse findAircraftDetails(@PathVariable @Valid String model) {
        return aircraftService.findByModelAircraft(model);
    }

    @Operation(summary = "Edita uma aeronave existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeronave atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aeronave não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{model}")
    @ResponseStatus(HttpStatus.OK)
    public AircraftDtoResponse editAircraft(@PathVariable String model,
                                            @RequestBody @Valid AircraftDtoRequest aircraftRequest) {
        return aircraftService.editAircraft(model, aircraftRequest);
    }

    @Operation(summary = "Remove uma aeronave pelo modelo")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aeronave removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aeronave não encontrada")
    })
    @DeleteMapping("/{model}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAircraft(@PathVariable @Valid String model) {
        aircraftService.deleteAircraft(model);
    }


}


