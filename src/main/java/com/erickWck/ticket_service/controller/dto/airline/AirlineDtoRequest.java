package com.erickWck.ticket_service.controller.dto.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record AirlineDtoRequest(

        @NotBlank(message = "O nome da companhia aérea é obrigatório.")
        @Size(max = 100, message = "O nome da companhia aérea deve ter no máximo 100 caracteres.")
        String name,

        @NotBlank(message = "O código ICAO é obrigatório.")
        @Size(min = 3, max = 4, message = "O código ICAO deve ter entre 3 e 4 caracteres.")
        String iacaoCode

) {
}
