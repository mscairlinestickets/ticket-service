package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.domain.exception.*;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.mapper.FlightMapper;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import com.erickWck.ticket_service.domain.service.flightFunctions.FlightValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplementsTest {

    @Mock
    private AirlineRepository airLineRepository;

    @Mock
    private AircraftRepository aircraftRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightValidator flightValidator;

    @InjectMocks
    private FlightServiceImplements flightService;

    @Captor
    private ArgumentCaptor<String> captor;

    @Nested
    class createdFlight {

        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;

        @BeforeEach
        void setup() {
            request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1),
                    180, 180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
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
            doThrow(new BusinessRuleException("Já existe um voo com esse número e horário para outro destino.")).when(flightValidator).validateFlight(any(Flight.class));
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
            var dtoRequest = new FlightDtoRequest("GOL123", "REU", "AIG", LocalDateTime.now().minusMinutes(5), 180, 180, BigDecimal.valueOf(342.99), "GOL", "B320");

            var airline = new Airline(1L, "GOL", "GOL");
            var aircraft = new Aircraft(1L, "B320", "Airbus",180);

            when(airLineRepository.findByIcaoCode("GOL")).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel("B320")).thenReturn(Optional.of(aircraft));
            doThrow(new BusinessRuleException("A data de partida deve ser no futuro.")).when(flightValidator).validateFlight(any(Flight.class));
            // act & assert
            var exception = assertThrows(BusinessRuleException.class, () -> {
                flightService.createFlight(dtoRequest);
            });

            assertEquals("A data de partida deve ser no futuro.", exception.getMessage());
        }

        @Test
        void shouldThrowBusinessRuleExceptionWhenDepartureIsDuplicate() {
            // arrange
            var dtoRequest = new FlightDtoRequest("GOL123", "REU", "AIG", LocalDateTime.now().plusDays(1), 180, 180, BigDecimal.valueOf(342.99), "GOL", "B320");

            var airline = new Airline(1L, "GOL", "GOL");
            var aircraft = new Aircraft(1L, "B320", "Airbus",180);

            when(airLineRepository.findByIcaoCode("GOL")).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel("B320")).thenReturn(Optional.of(aircraft));
            doThrow(new BusinessRuleException("Voo já cadastrado com esse número e horário.")).when(flightValidator).validateFlight(any(Flight.class));
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
                    () -> assertEquals(request.price(), result.price()),
                    () -> assertEquals(airline.name(), result.airlineName()),
                    () -> assertEquals(airline.icaoCode(), result.icaoCode()),
                    () -> assertEquals(aircraft.model(), result.aircraftModel()),
                    () -> assertEquals(aircraft.seatCapacity(), result.aircraftCapacity()));
        }


    }

    @Nested
    class findById {

        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;

        @BeforeEach
        void setup() {
            request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1), 180, 180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);

        }

        @Test
        void shouldReturnFlightWhenExist() {
            String flyNumber = "LAT123";
            //arrange
            when(flightRepository.findByFlightNumber(flyNumber)).thenReturn(Optional.of(flight));

            //act
            var result = flightService.findFlightNumber(flyNumber);

            //assert
            assertEquals(flyNumber, result.flightNumber());
            assertionsRequestWithDtoResponse(result);
        }

        @Test
        void shouldThrowAlreadyExceptionWhenAlreadyExist() {
            String flyNumber = "LAT123";
            String model = "A320";
            String icao = "TAM";
            //arrange
            when(airLineRepository.findByIcaoCode(icao)).thenReturn(Optional.of(airline));
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.of(aircraft));
            when(flightRepository.findByFlightNumber(flyNumber)).thenReturn(Optional.of(flight));

            //act
            var messageException = assertThrows(FlightAlreadyExist.class, () -> {
                flightService.createFlight(request);
            });

            //assert
            String message = "Flight with number: " + flyNumber + " already exist.";
            assertEquals(message, messageException.getMessage());
        }

        @Test
        void shouldThrowFlightNotFoundExceptionWhenDoesNotExists() {
            //arrange
            String flyNumber = "LAT123";
            doReturn(Optional.empty()).when(flightRepository).findByFlightNumber(flyNumber);

            //act
            var messageException = assertThrows(FlightNotFound.class, () -> {
                flightService.findFlightNumber(flyNumber);
            });

            //assert
            String message = "Flight with fly number: " + flyNumber + " not found.";
            assertEquals(message, messageException.getMessage());
        }

        private void assertionsRequestWithDtoResponse(FlightDtoResponse result) {
            assertAll("flight dto response",
                    () -> assertEquals(request.flightNumber(), result.flightNumber()),
                    () -> assertEquals(request.origin(), result.origin()),
                    () -> assertEquals(request.destination(), result.destination()),
                    () -> assertEquals(request.departureDateTime(), result.departureDateTime()),
                    () -> assertEquals(request.totalSeats(), result.totalSeats()),
                    () -> assertEquals(request.availableSeats(), result.availableSeats()),
                    () -> assertEquals(request.price(), result.price()),
                    () -> assertEquals(airline.name(), result.airlineName()),
                    () -> assertEquals(airline.icaoCode(), result.icaoCode()),
                    () -> assertEquals(aircraft.model(), result.aircraftModel()),
                    () -> assertEquals(aircraft.seatCapacity(), result.aircraftCapacity()));
        }
    }


    @Nested
    class findAll {
        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;

        @BeforeEach
        void setup() {
            request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1), 180, 180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);
        }

        @Test
        void shouldReturnListFlight() {
            //arrange
            var flightRequest = new FlightDtoRequest("GOL987", "CGH", "BSB", LocalDateTime.now().plusDays(2),
                    150, 150, BigDecimal.valueOf(439.90), "GOL", "B737");
            var airline2 = new Airline(2L, "GOL Linhas Aéreas", "GOL");
            var aircraft2 = new Aircraft(2L, "B737", "Airbus",150);
            var flight2 = FlightMapper.dtoToEntity(flightRequest, airline2, aircraft2);

            List<Flight> list = new ArrayList<>();
            list.add(flight);
            list.add(flight2);
            when(flightRepository.findAll()).thenReturn(list);

            //act
            var result = flightService.findAllFlight();

            //assert
            verify(flightRepository, times(1)).findAll();
            assertEquals(2, result.size());
            assertEquals("LAT123", flight.getFlightNumber());
            assertEquals("GOL987", flight2.getFlightNumber());
        }


        @Test
        void shouldReturnEmptyListWhenNoFlightsExist() {
            //arrange
            List<Flight> list = new ArrayList<>();
            when(flightRepository.findAll()).thenReturn(list);

            //act
            var result = flightService.findAllFlight();

            //assert
            assertTrue(result.isEmpty());
            verify(flightRepository, times(1)).findAll();
        }

    }

    @Nested
    class update {
        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;


        @BeforeEach
        void setup() {
            request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1), 180,
                    180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);
        }

        @Test
        void shouldUpdateFlightWhenItExists() {
            // arrange
            String flyNumber = "LAT123";

            var flightUpdate = new FlightDtoRequest(
                    flyNumber, "SP", "RJ", LocalDateTime.now().plusDays(2),
                    170, 160, BigDecimal.valueOf(339.90), "TAM", "A320"
            );

            var existingAirline = new Airline(1L, "LATAM", "TAM");
            var existingAircraft = new Aircraft(1L, "A320", "Airbus",180);

            var existingFlight = Flight.builder()
                    .id(1L).flightNumber(flyNumber).origin("GRU").destination("GIG")
                    .departureDateTime(LocalDateTime.now().plusDays(1)).totalSeats(180)
                    .availableSeats(180).price(BigDecimal.valueOf(585.99)).airline(existingAirline)
                    .aircraft(existingAircraft).build();

            when(flightRepository.findByFlightNumber(flyNumber)).thenReturn(Optional.of(existingFlight));
            when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // act
            var result = flightService.editFlight(flyNumber, flightUpdate);

            // assert
            assertEquals("SP", result.origin());
            assertEquals("RJ", result.destination());
            assertEquals(BigDecimal.valueOf(339.90), result.price());
            assertEquals("A320", result.aircraftModel());
            assertEquals("TAM", result.icaoCode());
            assertEquals("LAT123", result.flightNumber());

            verify(flightRepository, times(1)).findByFlightNumber(flyNumber);
            verify(flightRepository, times(1)).save(any(Flight.class));
        }

        @Test
        void shouldThrowFlightNotFoundExceptionWhenDoesNotExists() {
            //assert
            String flyNumber = "GLA123";
            doReturn(Optional.empty()).when(flightRepository).findByFlightNumber(flyNumber);

            //act

            var messageException = assertThrows(FlightNotFound.class, () -> {
                flightService.editFlight(flyNumber, request);
            });

            //assert
            String message = "Flight with fly number: " + flyNumber + " not found.";
            assertEquals(message, messageException.getMessage());
            verify(flightRepository, never()).save(any(Flight.class));
        }

    }

    @Nested
    class delete {

        private FlightDtoRequest request;
        private Airline airline;
        private Aircraft aircraft;
        private Flight flight;


        @BeforeEach
        void setup() {
            request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1), 180,
                    180, BigDecimal.valueOf(585.99), "TAM", "A320");
            airline = new Airline(1L, "LATAM", "TAM");
            aircraft = new Aircraft(1L, "A320", "Airbus",180);
            flight = FlightMapper.dtoToEntity(request, airline, aircraft);
        }

        @Test
        void shouldDeleteFlightWhenExist() {
            //arrange
            String flyNumber = "LAT123";
            when(flightRepository.findByFlightNumber(flyNumber)).thenReturn(Optional.of(flight));

            //act
            flightService.delete(flyNumber);

            //assert
            verify(flightRepository, times(1)).deleteByFlightNumber(captor.capture());
            assertEquals(flyNumber, captor.getValue());
        }

        @Test
        void shouldThrowExceptionWhenFlightDoesNotExist() {
            //arrange
            String flyNumber = "LAT123";

            //act
            var messageException = assertThrows(FlightNotFound.class, () -> {
                flightService.delete(flyNumber);
            });

            //assert
            var message = "Flight with fly number: " + flyNumber + " not found.";
            assertEquals(message, messageException.getMessage());
        }
    }


}