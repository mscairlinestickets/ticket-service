package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Gestão de Voos", description = "API responsável por realizar e administrar a gestão de voos.")
@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Cria um novo voo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FlightDtoResponse createNewFlight(@RequestBody @Valid FlightDtoRequest flightRequest) {
        return flightService.createFlight(flightRequest);
    }

    @Operation(summary = "Lista todos os voos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de voos retornada com sucesso")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FlightDtoResponse> listAllFlights() {
        return flightService.findAllFlight();
    }

    @Operation(summary = "Consulta os detalhes de um voo pelo número")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voo encontrado"),
            @ApiResponse(responseCode = "404", description = "Voo não encontrado")
    })
    @GetMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.OK)
    public FlightDtoResponse findFlightDetails(@PathVariable @Valid String flightNumber) {
        return flightService.findFlightNumber(flightNumber);
    }

    @Operation(summary = "Atualiza os dados de um voo existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Voo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Voo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.OK)
    public FlightDtoResponse updateFlightsExist(@PathVariable @Valid String flightNumber,
                                                @RequestBody @Valid FlightDtoRequest request) {
        return flightService.editFlight(flightNumber, request);
    }

    @Operation(summary = "Remove um voo pelo número")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Voo removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Voo não encontrado")
    })
    @DeleteMapping("/{flightNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFlightsExist(@PathVariable @Valid String flightNumber) {
        flightService.delete(flightNumber);
    }
}
