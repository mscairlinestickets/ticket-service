package com.erickWck.ticket_service.domain.service.flightFunctions;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.BusinessRuleException;
import com.erickWck.ticket_service.domain.mapper.FlightMapper;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightValidatorTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightValidator flightValidator;

    @Captor
    private ArgumentCaptor<Flight> captor;

    private FlightDtoRequest request;
    private Airline airline;
    private Aircraft aircraft;
    private Flight flight;

    @BeforeEach
    void setup() {
        request = new FlightDtoRequest("LAT123", "GRU", "GIG",
                LocalDateTime.now().plusMinutes(30), 180, 180, BigDecimal.valueOf(765.99),"TAM", "A320");
        airline = new Airline(1L, "LATAM", "TAM");
        aircraft = new Aircraft(1L, "A320", "Airbus",180);
        flight = FlightMapper.dtoToEntity(request, airline, aircraft);
    }


    @Test
    @DisplayName("Deve validar voo com sucesso quando todas as regras são atendidas")
    void shouldValidateSuccessfullyWhenAllRulesSuccess() {
        //arrange
        when(flightRepository.existsByFlightNumberAndDepartureDateTimeAndDestinationNot(flight.getFlightNumber(),
                flight.getDepartureDateTime(), flight.getDestination())).thenReturn(false);

        when(flightRepository.existsByFlightNumberAndDepartureDateTime(flight.getFlightNumber(),
                flight.getDepartureDateTime())).thenReturn(false);

        //act e assert
        assertDoesNotThrow(() -> flightValidator.validateFlight(flight));

    }

    @Test
    @DisplayName("Não deve lançar exceção se data de partida for exatamente agora")
    void shouldNotThrowIfDepartureIsExactlyNow() {

        //arrange
        var flight = createFlight(LocalDateTime.now().plusSeconds(1), "GIG");
        when(flightRepository.existsByFlightNumberAndDepartureDateTimeAndDestinationNot(flight.getFlightNumber(),
                flight.getDepartureDateTime(), flight.getDestination())).thenReturn(false);

        when(flightRepository.existsByFlightNumberAndDepartureDateTime(flight.getFlightNumber(),
                flight.getDepartureDateTime())).thenReturn(false);

        //act e assert
        assertDoesNotThrow(() -> flightValidator.validateFlight(flight));
    }


    @Test
    @DisplayName("Valida se o voo está com a data no passado.")
    void shouldThrowBusinessRuleExceptionWhenDepartureDatePast() {
        //arrange
        var flight2 = createFlight(LocalDateTime.now().minusMinutes(5), "SP");

        //act
        var messageException = assertThrows(BusinessRuleException.class, () -> {
            flightValidator.validateFlight(flight2);
        });

        String message = "A data de partida deve ser no futuro.";
        assertEquals(message, messageException.getMessage());
    }

    @Test
    @DisplayName("Valida se tem um voo, com mesmo numero, data e destino, para outro destino, impossibilitando cadastrar o voo.")
    void shouldThrowExceptionWhenExistsByFlightDuplicatedWithDestinationDifferent() {
        //arrange
        var flight2 = createFlight(LocalDateTime.now().plusDays(1), "SP");
        when(flightRepository.existsByFlightNumberAndDepartureDateTimeAndDestinationNot
                (flight2.getFlightNumber(), flight2.getDepartureDateTime(), flight2.getDestination())).thenReturn(true);

        //act
        var messageException = assertThrows(BusinessRuleException.class, () -> {
            flightValidator.validateFlight(flight2);
        });

        String message = "Já existe um voo com esse número e horário para outro destino.";
        assertEquals(message, messageException.getMessage());
    }

    @Test
    @DisplayName("Valida se já tem um voo cadastrado com a mesma requisição.")
    void shouldThrowBusinessRuleExceptionWhenExistsFlightIsDuplicated() {
        //arrange
        var flight2 = createFlight(LocalDateTime.now().plusDays(1), "RJ");

        when(flightRepository.existsByFlightNumberAndDepartureDateTime(flight2.getFlightNumber(),
                flight2.getDepartureDateTime())).thenReturn(true);

        //act
        var messageException = assertThrows(BusinessRuleException.class, () -> {
            flightValidator.validateFlight(flight2);
        });

        String message = "Voo já cadastrado com esse número e horário.";
        assertEquals(message, messageException.getMessage());
    }

    private Flight createFlight(LocalDateTime departureDateTime, String destination) {
        var request = new FlightDtoRequest("LAT123", "GRU", destination,
                departureDateTime, 180, 180, BigDecimal.valueOf(265.99),"TAM", "A320");
        var airline = new Airline(1L, "LATAM", "TAM");
        var aircraft = new Aircraft(1L, "A320", "Airbus",180);
        return FlightMapper.dtoToEntity(request, airline, aircraft);
    }

}