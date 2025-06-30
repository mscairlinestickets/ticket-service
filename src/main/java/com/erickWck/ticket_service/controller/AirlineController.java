package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.service.contract.AirlineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Gestão de Companhias Aéreas", description = "API responsável por realizar e administrar a gestão de companhias aéreas.")
@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    private final AirlineService airlineService;

    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    @Operation(summary = "Cria uma nova companhia aérea")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Companhia criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AirlineDtoResponse createNewAirlines(@RequestBody @Valid AirlineDtoRequest airlineRequest) {
        return airlineService.createAirline(airlineRequest);
    }

    @Operation(summary = "Lista todas as companhias aéreas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AirlineDtoResponse> listAllAirlines() {
        return airlineService.findAllAirline();
    }

    @Operation(summary = "Consulta os detalhes de uma companhia aérea pelo código ICAO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Companhia encontrada"),
            @ApiResponse(responseCode = "404", description = "Companhia não encontrada")
    })
    @GetMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.OK)
    public AirlineDtoResponse getFindByIdAirlines(@PathVariable @Valid String icaoCode) {
        return airlineService.findByAirline(icaoCode);
    }

    @Operation(summary = "Atualiza os dados de uma companhia aérea existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Companhia atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Companhia não encontrada")
    })
    @PutMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.OK)
    public AirlineDtoResponse updateAirlinesExist(@PathVariable @Valid String icaoCode, @RequestBody @Valid AirlineDtoRequest request) {
        return airlineService.editAirline(icaoCode, request);
    }

    @Operation(summary = "Remove uma companhia aérea pelo código ICAO")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Companhia removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Companhia não encontrada")
    })
    @DeleteMapping("/{icaoCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAirlinesExist(@PathVariable @Valid String icaoCode) {
        airlineService.delete(icaoCode);
    }
}

