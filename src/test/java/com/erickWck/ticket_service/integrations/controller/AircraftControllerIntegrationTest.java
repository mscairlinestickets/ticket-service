package com.erickWck.ticket_service.integrations.controller;

import com.erickWck.ticket_service.TestcontainersServiceConfiguration;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Import({TestcontainersServiceConfiguration.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AircraftControllerIntegrationTest {

    public static final String MODEL = "Air320";

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        aircraftRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/aircrafts")
    class PostAircraft {

        @Test
        @DisplayName("Deve criar uma nova aeronave com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldCreateNewAircraft() throws Exception {
            var request = new AircraftDtoRequest(MODEL, "Boeing", 180);
            var aircraft = new Aircraft(null, MODEL, "Boeing", 180,Instant.now(), Instant.now(), 0);

            mockMvc.perform(post("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.model").value(aircraft.getModel()))
                    .andExpect(jsonPath("$.manufacturer").value(aircraft.getManufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(aircraft.getSeatCapacity()));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar criar uma aeronave duplicada")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenAircraftAlreadyExists() throws Exception {
            var request = new AircraftDtoRequest(MODEL, "Boeing", 180);
            var aircraft = new Aircraft(null, MODEL, "Boeing", 180,Instant.now(), Instant.now(), 0);
            aircraftRepository.save(aircraft);

            mockMvc.perform(post("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string("Aircraft with model: " + MODEL + " already exist."));
        }
    }

    @Nested
    @DisplayName("GET /api/aircrafts and /api/aircrafts/{model}")
    class GetAircrafts {

        @Test
        @DisplayName("Deve listar todas as aeronaves")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldListAllAircrafts() throws Exception {
            var aircraft = new Aircraft(null, MODEL, "Boeing", 180,Instant.now(), Instant.now(), 0);
            var aircraft2 = new Aircraft(null, "TSF750", "Airbus", 280,Instant.now(), Instant.now(), 0);

            aircraftRepository.save(aircraft);
            aircraftRepository.save(aircraft2);

            mockMvc.perform(get("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].model").value(aircraft.getModel()))
                    .andExpect(jsonPath("$[0].manufacturer").value(aircraft.getManufacturer()))
                    .andExpect(jsonPath("$[0].seatCapacity").value(aircraft.getSeatCapacity()))
                    .andExpect(jsonPath("$[1].model").value(aircraft2.getModel()))
                    .andExpect(jsonPath("$[1].manufacturer").value(aircraft2.getManufacturer()))
                    .andExpect(jsonPath("$[1].seatCapacity").value(aircraft2.getSeatCapacity()));
        }

        @Test
        @DisplayName("Deve retornar os detalhes de uma aeronave existente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnAircraftDetails() throws Exception {
            var aircraft = new Aircraft(null, MODEL, "Boeing", 180,Instant.now(), Instant.now(), 0);
            aircraftRepository.save(aircraft);

            mockMvc.perform(get("/api/aircrafts/" + MODEL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.model").value(aircraft.getModel()))
                    .andExpect(jsonPath("$.manufacturer").value(aircraft.getManufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(aircraft.getSeatCapacity()));
        }

        @Test
        @DisplayName("Deve retornar erro ao buscar uma aeronave inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenAircraftNotFound() throws Exception {
            mockMvc.perform(get("/api/aircrafts/" + MODEL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Aircraft with model: " + MODEL + " not found."));
        }

        @Test
        @DisplayName("Deve retornar erro 405 ao acessar GET com path incompleto")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnMethodNotAllowedWhenPathVariableMissing() throws Exception {
            mockMvc.perform(get("/api/aircrafts/")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is4xxClientError());
        }

        @Test
        @DisplayName("Deve retornar 404 ao acessar rota inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnNotFoundForInvalidPath() throws Exception {
            mockMvc.perform(get("/api/aircraftsss")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("PUT /api/aircrafts/{model}")
    class PutAircrafts {

        @Test
        @DisplayName("Deve atualizar os dados da aeronave com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void whenUpdateRequestThenEditAircraft() throws Exception {

            var request = new AircraftDtoRequest(MODEL, "Boeing", 180);

            mockMvc.perform(post("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.model").value(request.model()))
                    .andExpect(jsonPath("$.manufacturer").value(request.manufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(request.seatCapacity()));


            var aircraftUpdate = new AircraftDtoRequest(request.model(), "Embraer", 320);

            mockMvc.perform(put("/api/aircrafts/" + MODEL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(aircraftUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.model").value(aircraftUpdate.model()))
                    .andExpect(jsonPath("$.manufacturer").value(aircraftUpdate.manufacturer()));

        }

        @Test
        @DisplayName("Deve retornar erro ao tentar editar uma aeronave inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void whenGetRequestExceptionEditAircraftNotFound() throws Exception {
            var request = new AircraftDtoRequest(MODEL, "Boeing", 180);

            var aircraftUpdate = new AircraftDtoRequest(request.model(), "Embraer", 320);

            mockMvc.perform(put("/api/aircrafts/" + MODEL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(aircraftUpdate)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Aircraft with model: " + MODEL + " not found."));
        }
    }

    @Nested
    @DisplayName("DELETE /api/aircrafts/{model}")
    class DeleteAircrafts {


        @Test
        @DisplayName("Deve deletar uma aeronave com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void whenDeleteRequestDeleteAircraft() throws Exception {
            var aircraft = new Aircraft(null, "Air320", "Boeing", 180,Instant.now(), Instant.now(), 0);
            aircraftRepository.save(aircraft);

            mockMvc.perform(delete("/api/aircrafts/" + aircraft.getModel()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar deletar uma aeronave inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void whenDeleteRequestThrowExceptionAircraftNotFoundDeleteIsNotFound() throws Exception {
            var aircraft = new Aircraft(null, MODEL, "Boeing", 180, Instant.now(), Instant.now(), 0);

            mockMvc.perform(delete("/api/aircrafts/" + MODEL))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Aircraft with model: " + MODEL + " not found."));
        }

    }

    @Test
    @DisplayName("Deve retornar 400 ao criar aeronave com dados inválidos")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnBadRequestForInvalidAircraftData() throws Exception {
        var request = new AircraftDtoRequest("", "", -1);
        mockMvc.perform(post("/api/aircrafts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


}