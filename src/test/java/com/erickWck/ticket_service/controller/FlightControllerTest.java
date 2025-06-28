package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.flight.FlightDtoRequest;
import com.erickWck.ticket_service.controller.dto.flight.FlightDtoResponse;
import com.erickWck.ticket_service.domain.exception.FlightAlreadyExist;
import com.erickWck.ticket_service.domain.exception.FlightNotFoundException;
import com.erickWck.ticket_service.domain.service.contract.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FlightService flightService;

    private FlightDtoRequest request;
    private FlightDtoResponse response;

    @BeforeEach
    void setup() {
        request = new FlightDtoRequest("LAT123", "GRU", "GIG", LocalDateTime.now().plusDays(1), 180, 180, BigDecimal.valueOf(585.99), "TAM", "A320");
        response = new FlightDtoResponse("VOO987", "São Paulo", "Florianópolis", LocalDateTime.of(2025, 9, 15, 14, 45), 180, 23, new BigDecimal("749.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);
    }

    @Nested
    @DisplayName("POST /api/flights")
    class createNewFlight {

        @Test
        @DisplayName("Deve criar o voo e retornar o status 201 - CREATED")
        void shouldCreateAirlineWhenDoesNotExist() throws Exception {
            //arrange
            var responseUpdate = new FlightDtoResponse(request.flightNumber(), request.origin(), request.destination(), request.departureDateTime().truncatedTo(ChronoUnit.SECONDS),
                    180, 23, new BigDecimal("485.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);
            when(flightService.createFlight(any())).thenReturn(responseUpdate);

            //act e assert
            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flightNumber").value(responseUpdate.flightNumber()))
                    .andExpect(jsonPath("$.origin").value(responseUpdate.origin()))
                    .andExpect(jsonPath("$.destination").value(responseUpdate.destination()))
                    .andExpect(jsonPath("$.departureDateTime").value(responseUpdate.departureDateTime().toString()))
                    .andExpect(jsonPath("$.totalSeats").value(responseUpdate.totalSeats()))
                    .andExpect(jsonPath("$.availableSeats").value(responseUpdate.availableSeats()))
                    .andExpect(jsonPath("$.price").value(responseUpdate.price()))
                    .andExpect(jsonPath("$.airlineName").value(responseUpdate.airlineName()))
                    .andExpect(jsonPath("$.icaoCode").value(responseUpdate.icaoCode()))
                    .andExpect(jsonPath("$.aircraftModel").value(responseUpdate.aircraftModel()))
                    .andExpect(jsonPath("$.aircraftCapacity").value(responseUpdate.aircraftCapacity()));

        }


        @Test
        @DisplayName("Deve retornar o erro 422 - UNPROCESS ENTITY  quando tentar cadastrar um voo flightNumber já existente.")
        void shouldError422WhenAirlinesAlreadyExist() throws Exception {
            //arrange
            String flyNumber = "GOL";
            var responseUpdate = new FlightDtoResponse(request.flightNumber(), request.origin(), request.destination(), request.departureDateTime().truncatedTo(ChronoUnit.SECONDS),
                    180, 23, new BigDecimal("485.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);
            doThrow(new FlightAlreadyExist(flyNumber)).when(flightService).createFlight(any());

            //act e assert
            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string("Flight with number: " + flyNumber + " already exist."));

        }
    }

    @Nested
    @DisplayName("GET /api/flights")
    class listAllFlights {

        @Test
        @DisplayName("Retorna uma lista de airlines com o código 200-Ok")
        void shoudlListOfFlight() throws Exception {
            //arrange
            var response2 = new FlightDtoResponse("VOO987", "São Paulo", "Florianópolis", LocalDateTime.of(2025, 9, 15, 14, 45), 180, 23, new BigDecimal("749.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);

            List<FlightDtoResponse> list = List.of(response, response2);
            when(flightService.findAllFlight()).thenReturn(list);

            //act e assert
            mockMvc.perform(get("/api/flights"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].flightNumber").value("VOO987"))
                    .andExpect(jsonPath("$[0].origin").value("São Paulo"))
                    .andExpect(jsonPath("$[0].destination").value("Florianópolis"))
                    .andExpect(jsonPath("$[0].departureDateTime").value("2025-09-15T14:45:00"))
                    .andExpect(jsonPath("$[0].totalSeats").value(180))
                    .andExpect(jsonPath("$[0].availableSeats").value(23))
                    .andExpect(jsonPath("$[0].price").value(749.99))
                    .andExpect(jsonPath("$[0].airlineName").value("LATAM Airlines"))
                    .andExpect(jsonPath("$[0].icaoCode").value("TAM"))
                    .andExpect(jsonPath("$[0].aircraftModel").value("Airbus A320"))
                    .andExpect(jsonPath("$[0].aircraftCapacity").value(180));

        }
    }

    @Nested
    @DisplayName("GET /api/flights/{flightNumber}")
    class getFindByIdFlights {

        @Test
        @DisplayName("Retorna um voo quando procurando pelo flightNumber")
        void shouldReturnFlightWhenExist() throws Exception {
            //arrange
            String flyNumber = "VOO987";
            when(flightService.findFlightNumber(flyNumber)).thenReturn(response);

            //act e assert
            mockMvc.perform(get("/api/flights/" + flyNumber))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(11))
                    .andExpect(jsonPath("$.flightNumber").value("VOO987"))
                    .andExpect(jsonPath("$.origin").value("São Paulo"))
                    .andExpect(jsonPath("$.destination").value("Florianópolis"))
                    .andExpect(jsonPath("$.departureDateTime").value("2025-09-15T14:45:00"))
                    .andExpect(jsonPath("$.totalSeats").value(180))
                    .andExpect(jsonPath("$.availableSeats").value(23))
                    .andExpect(jsonPath("$.price").value(749.99))
                    .andExpect(jsonPath("$.airlineName").value("LATAM Airlines"))
                    .andExpect(jsonPath("$.icaoCode").value("TAM"))
                    .andExpect(jsonPath("$.aircraftModel").value("Airbus A320"))
                    .andExpect(jsonPath("$.aircraftCapacity").value(180));
        }

        @Test
        @DisplayName("Retorna erro 404 - quando não encontra o voo pelo flightNumber")
        void shouldReturnError404NotFound() throws Exception {
            //arrange
            String flyNumber = "VOO987";
            doThrow(new FlightNotFoundException(flyNumber)).when(flightService).findFlightNumber(flyNumber);

            //act e assert
            mockMvc.perform(get("/api/flights/" + flyNumber))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + flyNumber + " not found."));
        }

    }

    @Nested
    @DisplayName("PUT /api/flights/{flightNumber}")
    class updateFlightsExist {

        @Test
        @DisplayName("Retorna o voo editado quando ele existir com um status 200 - OK")
        void shouldReturnFlightChangeWhenExist() throws Exception {
            //arrange
            String flyNumber = "LAT123";
            var responseUpdate = new FlightDtoResponse(request.flightNumber(), request.origin(), request.destination(), request.departureDateTime().truncatedTo(ChronoUnit.SECONDS),
                    180, 23, new BigDecimal("485.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);
            when(flightService.editFlight(eq(flyNumber), any(FlightDtoRequest.class)))
                    .thenReturn(responseUpdate);
            //act e assert
            mockMvc.perform(put("/api/flights/" + flyNumber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().is(200))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.flightNumber").value(responseUpdate.flightNumber()))
                    .andExpect(jsonPath("$.origin").value(responseUpdate.origin()))
                    .andExpect(jsonPath("$.destination").value(responseUpdate.destination()))
                    .andExpect(jsonPath("$.departureDateTime").value(responseUpdate.departureDateTime().toString()))
                    .andExpect(jsonPath("$.totalSeats").value(responseUpdate.totalSeats()))
                    .andExpect(jsonPath("$.availableSeats").value(responseUpdate.availableSeats()))
                    .andExpect(jsonPath("$.price").value(responseUpdate.price()))
                    .andExpect(jsonPath("$.airlineName").value(responseUpdate.airlineName()))
                    .andExpect(jsonPath("$.icaoCode").value(responseUpdate.icaoCode()))
                    .andExpect(jsonPath("$.aircraftModel").value(responseUpdate.aircraftModel()))
                    .andExpect(jsonPath("$.aircraftCapacity").value(responseUpdate.aircraftCapacity()));
        }

        @Test
        @DisplayName("Retorna o erro - 404 quando não encontra o voo para atualizar.")
        void shouldError404WhenFlightDoesNotExist() throws Exception {
            //arrange
            String flyNumber = "LAT123";
            var responseUpdate = new FlightDtoResponse(request.flightNumber(), request.origin(), request.destination(), request.departureDateTime().truncatedTo(ChronoUnit.SECONDS),
                    180, 23, new BigDecimal("485.99"), "LATAM Airlines", "TAM", "Airbus A320", 180);
            doThrow(new FlightNotFoundException(flyNumber)).when(flightService).editFlight(eq(flyNumber), any(FlightDtoRequest.class));

            //act e assert
            mockMvc.perform(put("/api/flights/" + flyNumber)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + flyNumber + " not found."));
        }
    }

    @Nested
    @DisplayName("DELETE /api/flights/{flightNumber}")
    class deleteFlightsExist {

        @Test
        @DisplayName("Deve deletar voo quando ele existir")
        void shouldDeleteAirlineWhenExist() throws Exception {
            //arrange
            String flyNumber = "LAT123";
            doNothing().when(flightService).delete(flyNumber);

            //act e arrange
            mockMvc.perform(delete("/api/flights/" + flyNumber))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar erro 404 ao tentar deletar voo inexistente")
        void shouldError404WhenAirlinesDoesExist() throws Exception {
            //arrange
            String flyNumber = "LAT123";
            doThrow(new FlightNotFoundException(flyNumber)).when(flightService).delete(flyNumber);

            //act e arrange
            mockMvc.perform(delete("/api/flights/" + flyNumber))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Flight with fly number: " + flyNumber + " not found."));
        }

    }

}