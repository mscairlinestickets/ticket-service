package com.erickWck.ticket_service.domain.service.flightFunctions;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.exception.NoAvailableSeatsException;
import com.erickWck.ticket_service.domain.mapper.FlightMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FlightFunctionsTest {


    @Nested
    class WhenAvailableSeats {

        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;

        @BeforeEach
        void setup() {
            double value = 435.43;
            request = new FlightDtoRequest("LAT123", "GRU", "GIG",
                    LocalDateTime.now().plusDays(1), 180, 180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);
        }

        @Test
        void shouldVerifyAvailableSeatsIsTrue() {
            //arrange
            boolean availables = FlightFunctions.hasAvailableSeats(flight);

            //act e arrange
            assertTrue(availables);
        }

        @Test
        void shouldVerifyAvailableSeatsIsFalse() {
            //arrange
            flight.setAvailableSeats(0);
            boolean availables = FlightFunctions.hasAvailableSeats(flight);

            //act e arrange
            assertFalse(availables);
        }

        @Test
        @DisplayName("Deve subtrair os assentos quando estiver disponivel.")
        void shouldDecrementWhenHasSeatsAvailable() {

            //arrange
            FlightFunctions.decrementAvailableSeats(flight);

            //act e assert
            assertEquals(179, flight.getAvailableSeats());
        }

        @Test
        @DisplayName("Deve subtrair os assentos quando estiver disponivel.")
        void shouldThrowExceptionWhenDoesNotExistsSeatsAvailable() {

            //arrange
            flight.setAvailableSeats(0);

            //arrange
            var messageException = assertThrows(NoAvailableSeatsException.class, () -> {
                FlightFunctions.decrementAvailableSeats(flight);
            });

            //assert
            String message = "Voo " + flight.getFlightNumber() + " está lotado. Não há assentos disponíveis.";
            assertEquals(message, messageException.getMessage());
        }
    }
}