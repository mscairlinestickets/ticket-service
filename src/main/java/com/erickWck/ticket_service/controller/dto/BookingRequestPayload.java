package com.erickWck.ticket_service.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequestPayload(
        @Schema(description = "Número do voo", example = "FL1234", required = true)
        @NotBlank(message = "O número do voo é obrigatório")
        String flightNumber,

        @Schema(description = "Quantidade de passagens solicitadas", example = "2", minimum = "1", required = true)
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser no mínimo 1")
        Integer quantity
) {
}
