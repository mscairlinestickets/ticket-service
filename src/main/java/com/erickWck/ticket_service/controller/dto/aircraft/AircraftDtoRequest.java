package com.erickWck.ticket_service.controller.dto.aircraft;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(toBuilder = true)
public record AircraftDtoRequest(

        @Schema(description = "Modelo da aeronave", example = "A320")
        @NotBlank(message = "O modelo da aeronave é obrigatório")
        String model,

        @Schema(description = "Fabricante da aeronave", example = "Airbus")
        @NotBlank(message = "O fabricante da aeronave é obrigatório")
        String manufacturer,

        @Schema(description = "Capacidade de assentos da aeronave", example = "180")
        @Positive(message = "A capacidade de assento deve ser maior que 0")
        int seatCapacity

) {
}