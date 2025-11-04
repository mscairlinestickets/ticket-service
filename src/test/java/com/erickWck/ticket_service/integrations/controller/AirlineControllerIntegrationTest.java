package com.erickWck.ticket_service.integrations.controller;


import com.erickWck.ticket_service.TestcontainersServiceConfiguration;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
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
class AirlineControllerIntegrationTest {

    public static final String ICAO = "GOL";

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        airlineRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/airlines")
    class PostAirline {

        @Test
        @DisplayName("Deve criar uma nova companhia aérea com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldCreateNewAirline() throws Exception {
            var request = new AirlineDtoRequest("Gol Linhas Aéreas", ICAO);

            mockMvc.perform(post("/api/airlines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(request.name()))
                    .andExpect(jsonPath("$.icaoCode").value(request.icaoCode()));
        }

        @Test
        @DisplayName("Deve retornar erro ao tentar criar companhia aérea duplicada")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenAirlineAlreadyExists() throws Exception {
            var airline = new Airline(null, "Gol Linhas Aéreas", ICAO,null,null,0);
            airlineRepository.save(airline);

            var request = new AirlineDtoRequest("Gol Linhas Aéreas", ICAO);

            mockMvc.perform(post("/api/airlines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string("Airline with icao code: " + ICAO + " already exist."));
        }
    }

    @Nested
    @DisplayName("GET /api/airlines e /api/airlines/{icaoCode}")
    class GetAirlines {

        @Test
        @DisplayName("Deve listar todas as companhias aéreas")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldListAllAirlines() throws Exception {
            airlineRepository.save(new Airline(null, "Gol", "GOL",null,null,0));
            airlineRepository.save(new Airline(null, "Latam", "LAT",null,null,0));

            mockMvc.perform(get("/api/airlines")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }

        @Test
        @DisplayName("Deve retornar detalhes da companhia aérea")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnAirlineDetails() throws Exception {
            airlineRepository.save(new Airline(null, "Gol", ICAO,Instant.now(), Instant.now(), 0));

            mockMvc.perform(get("/api/airlines/" + ICAO)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.icaoCode").value(ICAO))
                    .andExpect(jsonPath("$.name").value("Gol"));
        }

        @Test
        @DisplayName("Deve retornar erro ao buscar companhia inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenAirlineNotFound() throws Exception {
            mockMvc.perform(get("/api/airlines/" + ICAO))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + ICAO + " not found."));
        }
    }

    @Nested
    @DisplayName("PUT /api/airlines/{icaoCode}")
    class PutAirlines {

        @Test
        @DisplayName("Deve atualizar companhia aérea com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldUpdateAirline() throws Exception {
            airlineRepository.save(new Airline(null, "Gol", ICAO,Instant.now(), Instant.now(), 0));
            var request = new AirlineDtoRequest("TAM Airlines", ICAO);

            mockMvc.perform(put("/api/airlines/" + ICAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("TAM Airlines"))
                    .andExpect(jsonPath("$.icaoCode").value(ICAO));
        }

        @Test
        @DisplayName("Deve retornar erro ao atualizar companhia inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenUpdateNotFound() throws Exception {
            var request = new AirlineDtoRequest("Nova", ICAO);

            mockMvc.perform(put("/api/airlines/" + ICAO)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + ICAO + " not found."));
        }
    }

    @Nested
    @DisplayName("DELETE /api/airlines/{icaoCode}")
    class DeleteAirlines {

        @Test
        @DisplayName("Deve deletar companhia aérea com sucesso")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldDeleteAirline() throws Exception {
            airlineRepository.save(new Airline(null, "Gol", ICAO, Instant.now(), Instant.now(), 0));

            mockMvc.perform(delete("/api/airlines/" + ICAO))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar erro ao deletar companhia inexistente")
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void shouldReturnErrorWhenDeleteNotFound() throws Exception {
            mockMvc.perform(delete("/api/airlines/" + ICAO))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + ICAO + " not found."));
        }
    }
}

