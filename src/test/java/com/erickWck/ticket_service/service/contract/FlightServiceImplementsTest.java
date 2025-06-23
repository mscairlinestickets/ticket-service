package com.erickWck.ticket_service.service.contract;

import com.erickWck.ticket_service.dto.FlightDtoRequest;
import com.erickWck.ticket_service.dto.FlightDtoResponse;
import com.erickWck.ticket_service.entity.Aircraft;
import com.erickWck.ticket_service.entity.Airline;
import com.erickWck.ticket_service.entity.Flight;
import com.erickWck.ticket_service.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.exception.BusinessRuleException;
import com.erickWck.ticket_service.mapper.FlightMapper;
import com.erickWck.ticket_service.repository.AirLineRepository;
import com.erickWck.ticket_service.repository.AircraftRepository;
import com.erickWck.ticket_service.repository.FlightRepository;
import com.erickWck.ticket_service.service.FlightServiceImplements;
import com.erickWck.ticket_service.service.flight.FlightValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplementsTest {

    @Mock
    private AirLineRepository airLineRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightValidator flightValidator;

    @InjectMocks
    private FlightServiceImplements flightService;

    @Nested
    class createdFlight {

        FlightDtoRequest request;
        Airline airline;
        Aircraft aircraft;
        Flight flight;

        @BeforeEach
        void setup() {
            request = new FlightDtoRequest(
                    "LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1),
                    180, 180, "TAM", "A320"
            );
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", 180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);

        }

        @Test
        void shouldCreateFlightWhenDoesNotExist() {
            //arrange
            when(airLineRepository.findByIcaoCode(request.icaoCode())).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel(aircraft.model())).thenReturn(Optional.of(aircraft));
            when(flightRepository.save(any(Flight.class))).thenReturn(flight);


            //act
            var result = flightService.createFlight(request);

            //assert
            assertNotNull(result);
            assertionsRequestWithDtoResponse(result);
            verify(flightRepository, times(1)).save(any(Flight.class));
        }

        @Test
        void shouldThrowAirlineNotFoundExceptionWhenAirlineDoesNotExist() {
            //arrange
            when(airLineRepository.findByIcaoCode(request.icaoCode())).thenReturn(Optional.empty());

            //act e arrange
            var result = assertThrows(AirlineNotFoundException.class, () -> {
                flightService.createFlight(request);
            });

            String message = "Airline with ICAO code " + request.icaoCode() + " not found.";
            assertEquals(message, result.getMessage());
            verify(flightRepository, never()).save(any());
        }


        @Test
        void shouldThrowAircraftNotFoundExceptionWhenAircraftDoesNotExist() {
            //arrange
            when(airLineRepository.findByIcaoCode(request.icaoCode())).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel(request.model())).thenReturn(Optional.empty());

            //act e arrange
            var result = assertThrows(AircraftNotFoundException.class, () -> {
                flightService.createFlight(request);
            });

            String message = "Aircraft with model: " + request.model() + " not found.";
            assertEquals(message, result.getMessage());
            verify(flightRepository, never()).save(any());
        }

        @Test
        void shouldThrowBusinessRuleExceptionWhenExistsByFlightNumberAndDepartureDateTimeAndDestination() {
            //arrange
            when(airLineRepository.findByIcaoCode(request.icaoCode())).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel(aircraft.model())).thenReturn(Optional.of(aircraft));
            doThrow(new BusinessRuleException("Já existe um voo com esse número e horário para outro destino."))
                    .when(flightValidator).validateFlight(any(Flight.class));
            //act
            var messageException = assertThrows(BusinessRuleException.class, () -> {
                flightService.createFlight(request);
            });

            //assert
            String message = "Já existe um voo com esse número e horário para outro destino.";
            assertEquals(message, messageException.getMessage());
        }

        @Test
        void shouldThrowBusinessRuleExceptionWhenDepartureDoesNotFuture() {
            // arrange
            var dtoRequest = new FlightDtoRequest(
                    "GOL123", "REU", "AIG",
                    LocalDateTime.now().minusMinutes(5),
                    180, 180, "GOL", "B320"
            );

            var airline = new Airline(1L, "GOL", "GOL");
            var aircraft = new Aircraft(1L, "B320", 180);

            when(airLineRepository.findByIcaoCode("GOL")).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel("B320")).thenReturn(Optional.of(aircraft));
            doThrow(new BusinessRuleException("A data de partida deve ser no futuro."))
                    .when(flightValidator).validateFlight(any(Flight.class));
            // act & assert
            var exception = assertThrows(BusinessRuleException.class, () -> {
                flightService.createFlight(dtoRequest);
            });

            assertEquals("A data de partida deve ser no futuro.", exception.getMessage());
        }

        @Test
        void shouldThrowBusinessRuleExceptionWhenDepartureIsDuplicate() {
            // arrange
            var dtoRequest = new FlightDtoRequest(
                    "GOL123", "REU", "AIG",
                    LocalDateTime.now().plusDays(1),
                    180, 180, "GOL", "B320"
            );

            var airline = new Airline(1L, "GOL", "GOL");
            var aircraft = new Aircraft(1L, "B320", 180);

            when(airLineRepository.findByIcaoCode("GOL")).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel("B320")).thenReturn(Optional.of(aircraft));
            doThrow(new BusinessRuleException("Voo já cadastrado com esse número e horário."))
                    .when(flightValidator).validateFlight(any(Flight.class));
            // act & assert
            var exception = assertThrows(BusinessRuleException.class, () -> {
                flightService.createFlight(dtoRequest);
            });

            assertEquals("Voo já cadastrado com esse número e horário.", exception.getMessage());
        }


        private void assertionsRequestWithDtoResponse(FlightDtoResponse result) {
            assertAll("flight dto response",
                    () -> assertEquals(request.flightNumber(), result.flightNumber()),
                    () -> assertEquals(request.origin(), result.origin()),
                    () -> assertEquals(request.destination(), result.destination()),
                    () -> assertEquals(request.departureDateTime(), result.departureDateTime()),
                    () -> assertEquals(request.totalSeats(), result.totalSeats()),
                    () -> assertEquals(request.availableSeats(), result.availableSeats()),
                    () -> assertEquals(airline.name(), result.airlineName()),
                    () -> assertEquals(airline.icaoCode(), result.icaoCode()),
                    () -> assertEquals(aircraft.model(), result.aircraftModel()),
                    () -> assertEquals(aircraft.seatCapacity(), result.aircraftCapacity())
            );
        }


    }

}