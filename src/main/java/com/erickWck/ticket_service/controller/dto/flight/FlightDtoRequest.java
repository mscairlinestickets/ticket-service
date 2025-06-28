package com.erickWck.ticket_service.controller.dto.flight;

import com.erickWck.ticket_service.domain.entity.Flight;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record FlightDtoRequest(
        @NotBlank(message = "O número do voo é obrigatório.")
        String flightNumber,

        @NotBlank(message = "A origem é obrigatória.")
        String origin,

        @NotBlank(message = "O destino é obrigatório.")
        String destination,

        @NotNull(message = "A data e hora de partida são obrigatórias.")
        @Future(message = "A data de partida deve ser futura.")
        LocalDateTime departureDateTime,

        @Positive(message = "O número total de assentos deve ser maior que 0.")
        int totalSeats,

        @PositiveOrZero(message = "A quantidade de assentos disponíveis não pode ser negativa.")
        int availableSeats,

        @NotNull(message = "O preço é obrigatório.")
        @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0.")
        BigDecimal price,

        @NotBlank(message = "O código ICAO da companhia aérea é obrigatório.")
        String icaoCode,

        @NotBlank(message = "O modelo da aeronave é obrigatório.")
        String model

) {

    public static Flight dtoToEntity(FlightDtoRequest request) {
        return new Flight(null, request.flightNumber, request.origin, request.destination, request.departureDateTime,
                request.totalSeats, request.availableSeats, request.price, null, null);
    }

}
