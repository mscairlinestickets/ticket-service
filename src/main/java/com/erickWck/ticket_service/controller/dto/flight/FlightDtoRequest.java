package com.erickWck.ticket_service.controller.dto.flight;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FlightDtoRequest(

        @Schema(description = "Número identificador do voo", example = "TK1933")
        @NotBlank(message = "O número do voo é obrigatório.")
        String flightNumber,

        @Schema(description = "Cidade ou código de origem do voo", example = "GRU")
        @NotBlank(message = "A origem é obrigatória.")
        String origin,

        @Schema(description = "Cidade ou código de destino do voo", example = "LIS")
        @NotBlank(message = "O destino é obrigatório.")
        String destination,

        @Schema(description = "Data e hora da partida do voo", example = "2025-08-12T18:30:00")
        @NotNull(message = "A data e hora de partida são obrigatórias.")
        @Future(message = "A data de partida deve ser futura.")
        LocalDateTime departureDateTime,

        @Schema(description = "Número total de assentos da aeronave", example = "200", minimum = "1")
        @Positive(message = "O número total de assentos deve ser maior que 0.")
        int totalSeats,

        @Schema(description = "Número de assentos disponíveis para venda", example = "58", minimum = "1")
        @PositiveOrZero(message = "A quantidade de assentos disponíveis não pode ser negativa.")
        int availableSeats,

        @Schema(description = "Preço do bilhete", example = "1199.99", minimum = "1")
        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0.")
        BigDecimal price,

        @Schema(description = "Código ICAO da companhia aérea", example = "TAP")
        @NotBlank(message = "O código ICAO da companhia aérea é obrigatório.")
        String icaoCode,

        @Schema(description = "Modelo da aeronave associada ao voo", example = "A320neo")
        @NotBlank(message = "O modelo da aeronave é obrigatório.")
        String model

) {
}
