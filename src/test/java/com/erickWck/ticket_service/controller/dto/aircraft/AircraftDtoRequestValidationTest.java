package com.erickWck.ticket_service.controller.dto.aircraft;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AircraftDtoRequestValidationTest {

    private static Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private Set<ConstraintViolation<AircraftDtoRequest>> getConstraintViolations(AircraftDtoRequest request) {
        return validator.validate(request);
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        var dto = new AircraftDtoRequest("737 MAX", "Boeing", 189);
        var violations = getConstraintViolations(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenModelIsBlank_thenValidationFails() {
        var dto = new AircraftDtoRequest("   ", "Boeing", 189);
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("O modelo da aeronave é obrigatório");
    }

    @Test
    void whenManufacturerIsBlank_thenValidationFails() {
        var dto = new AircraftDtoRequest("737 MAX", " ", 189);
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("O fabricante da aeronave é obrigatório");
    }

    @Test
    void whenSeatCapacityIsZero_thenValidationFails() {
        var dto = new AircraftDtoRequest("737 MAX", "Boeing", 0);
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("A capacidade de assento deve ser maior que 0");
    }

    @Test
    void whenAllFieldsAreInvalid_thenReturnsAllErrors() {
        var dto = new AircraftDtoRequest("", "", -10);
        var violations = getConstraintViolations(dto);

        var messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(violations).hasSize(3);
        assertThat(messages).contains(
                "O modelo da aeronave é obrigatório",
                "O fabricante da aeronave é obrigatório",
                "A capacidade de assento deve ser maior que 0"
        );
    }
}
