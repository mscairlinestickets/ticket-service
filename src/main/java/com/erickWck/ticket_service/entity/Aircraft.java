package com.erickWck.ticket_service.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record Aircraft(

        Long uuid,

        @NotBlank(message = "Insira o modelo do avião.")
        String model,

        @NotBlank(message = "Insira a capacidade de assento.")
        @Positive(message = "O assento deve ser maior que zero.")
        int seatCapacity
) {
}
