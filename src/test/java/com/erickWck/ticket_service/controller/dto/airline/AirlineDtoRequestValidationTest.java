package com.erickWck.ticket_service.controller.dto.airline;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class AirlineDtoRequestValidationTest {

    private static Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenValidationSucceeds() {
        var dto = new AirlineDtoRequest("LATAM Airlines", "Bra");
        var violations = getConstraintViolations(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenNameIsBlank_thenValidationFails() {
        var dto = new AirlineDtoRequest("", "TAM");
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("O nome da companhia aérea é obrigatório.");
    }

    @Test
    void whenNameIsNull_thenValidationFails() {
        var dto = new AirlineDtoRequest(null, "TAM");
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("O nome da companhia aérea é obrigatório.");
    }

    @Test
    void whenIcaoCodeIsBlank_thenValidationFails() {
        var dto = new AirlineDtoRequest("Latam", "");
        var violations = getConstraintViolations(dto);

        var messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(violations).hasSize(2);
        assertThat(messages).contains(
                "O código ICAO é obrigatório.",
                "O código ICAO deve ter entre 3 e 4 caracteres."
        );
    }

    @Test
    void whenIcaoCodeIsNull_thenValidationFails() {
        var dto = new AirlineDtoRequest("Latam", null);
        var violations = getConstraintViolations(dto);

        var messages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertThat(violations).hasSize(1);
        assertThat(messages).contains(
                "O código ICAO é obrigatório."
        );
    }

    @Test
    void whenAllFieldsAreInvalid_thenReturnsAllErrors() {
        var dto = new AirlineDtoRequest("", "");
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(3);
    }

    @Test
    void whenAllFieldsAreNull_thenReturnsAllErrors() {
        var dto = new AirlineDtoRequest(null, null);
        var violations = getConstraintViolations(dto);

        assertThat(violations).hasSize(2);
    }


    private static Set<ConstraintViolation<AirlineDtoRequest>> getConstraintViolations(AirlineDtoRequest request) {
        Set<ConstraintViolation<AirlineDtoRequest>> violations = validator.validate(request);
        return violations;
    }
}
