package com.erickWck.ticket_service.controller.dto.flight;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FlightDtoRequestValidationTest {

    private static Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<FlightDtoRequest>> getViolations(FlightDtoRequest dto) {
        return validator.validate(dto);
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        var dto = FlightDtoRequest.builder()
                .flightNumber("VOO123")
                .origin("São Paulo")
                .destination("Rio de Janeiro")
                .departureDateTime(LocalDateTime.now().plusDays(1))
                .totalSeats(180)
                .availableSeats(50)
                .price(new BigDecimal("499.90"))
                .icaoCode("GOL")
                .model("Boeing 737")
                .build();

        var violations = getViolations(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenFieldsAreBlankOrInvalid_thenValidationFails() {
        var dto = FlightDtoRequest.builder()
                .flightNumber("")
                .origin("")
                .destination("   ")
                .departureDateTime(null)
                .totalSeats(0)
                .availableSeats(-10)
                .price(BigDecimal.ZERO)
                .icaoCode(" ")
                .model("")
                .build();

        var violations = getViolations(dto);
        var messages = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

        assertThat(violations).hasSize(9);
        assertThat(messages).contains(
                "O número do voo é obrigatório.",
                "A origem é obrigatória.",
                "O destino é obrigatório.",
                "A data e hora de partida são obrigatórias.",
                "O número total de assentos deve ser maior que 0.",
                "A quantidade de assentos disponíveis não pode ser negativa.",
                "O preço deve ser maior que 0.",
                "O código ICAO da companhia aérea é obrigatório.",
                "O modelo da aeronave é obrigatório."
        );
    }

    @Test
    void whenDepartureDateIsInPast_thenValidationFails() {
        var dto = FlightDtoRequest.builder()
                .flightNumber("VOO789")
                .origin("SP")
                .destination("RJ")
                .departureDateTime(LocalDateTime.now().minusHours(3))
                .totalSeats(120)
                .availableSeats(15)
                .price(new BigDecimal("599.90"))
                .icaoCode("AZU")
                .model("Embraer E195")
                .build();

        var violations = getViolations(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("A data de partida deve ser futura.");
    }

    @Test
    void whenPriceIsNull_thenValidationFails() {
        var dto = FlightDtoRequest.builder()
                .flightNumber("VOO001")
                .origin("SP")
                .destination("BH")
                .departureDateTime(LocalDateTime.now().plusHours(2))
                .totalSeats(80)
                .availableSeats(40)
                .price(null)
                .icaoCode("TAM")
                .model("A320")
                .build();

        var violations = getViolations(dto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("O preço é obrigatório.");
    }
}
