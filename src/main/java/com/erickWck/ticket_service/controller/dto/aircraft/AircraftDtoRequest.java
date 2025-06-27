package com.erickWck.ticket_service.controller.dto.aircraft;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder(toBuilder = true)
public record AircraftDtoRequest(

        @NotBlank(message = "O modelo da aeronave é obrigatório")
        String model,

        @NotBlank(message = "O fabricante da aeronave é obrigatório")
        String manufacturer,

        @Positive(message = "A capacidade de assento deve ser maior que 0")
        int seatCapacity
) {
}
