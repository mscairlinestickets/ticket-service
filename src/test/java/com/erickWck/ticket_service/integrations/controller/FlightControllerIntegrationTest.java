package com.erickWck.ticket_service.integrations.controller;

import com.erickWck.ticket_service.TestcontainersServiceConfiguration;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.entity.Flight;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import com.erickWck.ticket_service.domain.repository.FlightRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import(TestcontainersServiceConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;
    @Autowired
    private AircraftRepository aircraftRepository;

    private static final String FLIGHT_NUMBER = "AZU123";
    private Airline airline;
    private Aircraft aircraft;

    @BeforeEach
    void setup() {
        flightRepository.deleteAll();
        airlineRepository.deleteAll();
        aircraftRepository.deleteAll();

        airline = airlineRepository.save(new Airline(null, "Azul", "AZU", Instant.now(), Instant.now(), 0));
        aircraft = aircraftRepository.save(new Aircraft(null, "A320", "Airbus", 180, Instant.now(), Instant.now(), 0));
    }

    @Nested
    @DisplayName("POST /api/flights")
    class PostFlight {

        @Test
        @DisplayName("Deve criar um voo com sucesso")
        void shouldCreateFlightSuccessfully() throws Exception {

            var request = new FlightDtoRequest(
                    FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline.getIcaoCode(), aircraft.getModel());

            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                    .andExpect(jsonPath("$.origin").value(request.origin()))
                    .andExpect(jsonPath("$.destination").value(request.destination()))
                    .andExpect(jsonPath("$.departureDateTime").value(request.departureDateTime().toString()))
                    .andExpect(jsonPath("$.totalSeats").value(request.totalSeats()))
                    .andExpect(jsonPath("$.availableSeats").value(request.availableSeats()))
                    .andExpect(jsonPath("$.price").value(request.price()))
                    .andExpect(jsonPath("$.icaoCode").value(request.icaoCode()))
                    .andExpect(jsonPath("$.aircraftModel").value(request.model()));
        }


        @Test
        @DisplayName("Retorna erro 409 - CONFLICT, caso tente cadastrar o mesmo voo com numero, horário para outro destino.")
        void shouldExceptionWhenFlightFlightNumberIsExistDepartureAndOtherDestination() throws Exception {
            var request = new FlightDtoRequest(
                    FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline.getIcaoCode(), aircraft.getModel());

            var flightRequest = new Flight(null, FLIGHT_NUMBER, "GRU", "SP", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);
            flightRepository.save(flightRequest);

            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().string("Já existe um voo com esse número e horário para outro destino."));
        }

        @Test
        @DisplayName("Retorna erro 409 - CONFLICT, caso tente cadastrar o mesmo voo com numero e data de saida já existente.")
        void shouldExceptionWhenFlightFlightNumberIsExistAndDeparture() throws Exception {

            var request = new FlightDtoRequest(FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline.getIcaoCode(), aircraft.getModel());

            var flightRequest = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);

            flightRepository.save(flightRequest);

            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(content().string("Voo já cadastrado com esse número e horário."));
        }

    }

    @Nested
    @DisplayName("GET /api/flights e /api/flights/{flightNumber}")
    class GetFlights {

        @Test
        @DisplayName("Deve retornar a listagem de todos voo com o status 200 - Ok")
        void shouldListAllOfFlights() throws Exception {
            var flight = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);
            var flight2 = new Flight(null, FLIGHT_NUMBER, "SP", "RJ", LocalDateTime.now().plusDays(2).truncatedTo(ChronoUnit.SECONDS),
                    180, 150, new BigDecimal("499.99"), airline, aircraft, Instant.now(), Instant.now(), 0);

            flightRepository.save(flight);
            flightRepository.save(flight2);

            mockMvc.perform(get("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].flightNumber").value(FLIGHT_NUMBER))
                    .andExpect(jsonPath("$[0].origin").value(flight.getOrigin()))
                    .andExpect(jsonPath("$[0].destination").value(flight.getDestination()))
                    .andExpect(jsonPath("$[0].departureDateTime").value(flight.getDepartureDateTime().toString()))
                    .andExpect(jsonPath("$[0].totalSeats").value(flight.getTotalSeats()))
                    .andExpect(jsonPath("$[0].availableSeats").value(flight.getAvailableSeats()))
                    .andExpect(jsonPath("$[0].price").value(flight.getPrice()))
                    .andExpect(jsonPath("$[0].icaoCode").value(airline.getIcaoCode()))
                    .andExpect(jsonPath("$[0].aircraftModel").value(aircraft.getModel()));
        }

        @Test
        @DisplayName("Deve retornar os detalhes de uma aeronave existente")
        void shouldReturnFlightDetailsByFlightNumber() throws Exception {
            var flight = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);

            flightRepository.save(flight);

            mockMvc.perform(get("/api/flights/" + FLIGHT_NUMBER)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                    .andExpect(jsonPath("$.origin").value(flight.getOrigin()))
                    .andExpect(jsonPath("$.destination").value(flight.getDestination()))
                    .andExpect(jsonPath("$.departureDateTime").value(flight.getDepartureDateTime().toString()))
                    .andExpect(jsonPath("$.totalSeats").value(flight.getTotalSeats()))
                    .andExpect(jsonPath("$.availableSeats").value(flight.getAvailableSeats()))
                    .andExpect(jsonPath("$.price").value(flight.getPrice()))
                    .andExpect(jsonPath("$.icaoCode").value(airline.getIcaoCode()))
                    .andExpect(jsonPath("$.aircraftModel").value(aircraft.getModel()));
        }

        @Test
        @DisplayName("Deve retornar erro ao buscar um voo inexistente")
        void shouldReturnNotFoundWhenFlightDoesNotExist() throws Exception {
            var flight = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);

            mockMvc.perform(get("/api/flights/" + FLIGHT_NUMBER)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + FLIGHT_NUMBER + " not found."));
        }

    }

    @Nested
    @DisplayName("PUT /api/flights/{flightNumber}")
    class PutFlights {


        @Test
        @DisplayName("Deve atualizar os dados do voo com sucesso")
        void shouldUpdateFlightSuccessfully() throws Exception {

            var request = new FlightDtoRequest(FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline.getIcaoCode(), aircraft.getModel());


            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                    .andExpect(jsonPath("$.origin").value(request.origin()))
                    .andExpect(jsonPath("$.destination").value(request.destination()))
                    .andExpect(jsonPath("$.departureDateTime").value(request.departureDateTime().toString()));

            var requestUpdate = new FlightDtoRequest(request.flightNumber(), "SP", "BRA", LocalDateTime.now().plusDays(4).truncatedTo(ChronoUnit.SECONDS),
                    180, 150, new BigDecimal("399.99"), request.icaoCode(), request.model());

            mockMvc.perform(put("/api/flights/" + FLIGHT_NUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.flightNumber").value(FLIGHT_NUMBER))
                    .andExpect(jsonPath("$.origin").value(requestUpdate.origin()))
                    .andExpect(jsonPath("$.destination").value(requestUpdate.destination()))
                    .andExpect(jsonPath("$.departureDateTime").value(requestUpdate.departureDateTime().toString()))
                    .andExpect(jsonPath("$.totalSeats").value(requestUpdate.totalSeats()))
                    .andExpect(jsonPath("$.availableSeats").value(requestUpdate.availableSeats()))
                    .andExpect(jsonPath("$.price").value(requestUpdate.price()))
                    .andExpect(jsonPath("$.icaoCode").value(requestUpdate.icaoCode()))
                    .andExpect(jsonPath("$.aircraftModel").value(requestUpdate.model()));

        }

        @Test
        @DisplayName("Deve retornar error 404 - NOT FOUND ao tentar editar um voo inexistente")
        void shouldReturnNotFoundWhenUpdatingNonexistentFlight() throws Exception {

            var request = new FlightDtoRequest(FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline.getIcaoCode(), aircraft.getModel());


            mockMvc.perform(put("/api/flights/" + FLIGHT_NUMBER)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + FLIGHT_NUMBER + " not found."));
        }

    }

    @Nested
    @DisplayName("DELETE /api/flights/{flightNumber}")
    class DeleteFlights {

        @Test
        @DisplayName("Retornar o status NOT_CONTENT - 202  quando excluir um voo  com sucesso")
        void shouldDeleteFlightSuccessfully() throws Exception {
            var flight = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);

            flightRepository.save(flight);

            mockMvc.perform(delete("/api/flights/" + FLIGHT_NUMBER))
                    .andExpect(status().isNoContent());

            assertThat(flightRepository.findByFlightNumber(FLIGHT_NUMBER).isPresent()).isFalse();
        }

        @Test
        @DisplayName("Retornar erro 404 - NOT FOUND ao tentar deletar um voo inexistente")
        void shouldReturnNotFoundWhenDeletingNonexistentFlight() throws Exception {
            var flight = new Flight(null, FLIGHT_NUMBER, "GRU", "REC", LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180, 180, new BigDecimal("899.99"), airline, aircraft, Instant.now(), Instant.now(), 0);


            mockMvc.perform(delete("/api/flights/" + FLIGHT_NUMBER))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + FLIGHT_NUMBER + " not found."));

            assertThat(flightRepository.findByFlightNumber(FLIGHT_NUMBER).isPresent()).isFalse();
        }

    }

    @Nested
    @DisplayName("POST /api/flights - Validações de dados inválidos")
    class InvalidFlightData {

        @Test
        @DisplayName("Deve retornar 400 - Bad Request quando campos obrigatórios estiverem ausentes")
        void shouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {

            var invalidRequest = new FlightDtoRequest(null, null, null, null,
                    0, 0, null, null, null);


            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 - Bad Request quando data de partida estiver no passado")
        void shouldReturnBadRequestWhenDepartureDateTimeIsInThePast() throws Exception {
            var request = new FlightDtoRequest(
                    FLIGHT_NUMBER,
                    "GRU",
                    "REC",
                    LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180,
                    180,
                    new BigDecimal("899.99"),
                    airline.getIcaoCode(),
                    aircraft.getModel()
            );

            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }


        @Test
        @DisplayName("Deve retornar 400 - Bad Request quando o preço for negativo")
        void shouldReturnBadRequestWhenPriceIsNegative() throws Exception {
            var request = new FlightDtoRequest(
                    FLIGHT_NUMBER,
                    "GRU",
                    "REC",
                    LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS),
                    180,
                    180,
                    new BigDecimal("-100.00"),
                    airline.getIcaoCode(),
                    aircraft.getModel()
            );

            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

}
