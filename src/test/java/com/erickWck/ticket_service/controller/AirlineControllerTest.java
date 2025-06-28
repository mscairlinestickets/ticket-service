package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoResponse;
import com.erickWck.ticket_service.domain.exception.AirlineAlreadyExist;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.service.contract.AirlineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AirlineController.class)
class AirlineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AirlineService airlineService;

    @Nested
    @DisplayName("GET /api/airlines")
    class listAllAirlines {


        @Test
        @DisplayName("Deve retornar todas companhia aérea com status 200")
        void shouldReturnListOfAirlines() throws Exception {
            //arrange
            List<AirlineDtoResponse> list = List.of(new AirlineDtoResponse("FlyGold Airlines", "FGI"),
                    new AirlineDtoResponse("FlySilver Airlines", "FGS"));

            when(airlineService.findAllAirline()).thenReturn(list);

            //act e assert
            mockMvc.perform(get("/api/airlines"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(list.size()))
                    .andExpect(jsonPath("$[0].name").value("FlyGold Airlines"))
                    .andExpect(jsonPath("$[0].icaoCode").value("FGI"))
                    .andExpect(jsonPath("$[1].name").value("FlySilver Airlines"))
                    .andExpect(jsonPath("$[1].icaoCode").value("FGS"));
        }

    }

    @Nested
    @DisplayName("GET /api/airlines/{name}")
    class getFindByIdAirlines {

        @Test
        @DisplayName("Deve retornar a companhia aérea quando ela existir, com status 200")
        void shouldReturnAirlineWhenExists() throws Exception {
            //arrange
            String icaoCode = "FGI";
            AirlineDtoResponse airlineDtoResponse = new AirlineDtoResponse("FlyGold Airlines", "FGI");
            when(airlineService.findByAirline(icaoCode)).thenReturn(airlineDtoResponse);

            //act e assert
            mockMvc.perform(get("/api/airlines/" + icaoCode))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value("FlyGold Airlines"))
                    .andExpect(jsonPath("$.icaoCode").value("FGI"));
        }

        @Test
        @DisplayName("Deve lançar erro 404 quando airlin não existir")
        void shouldThrowAircraftNotFoundWhenDoesNotExist() throws Exception {
            //ararnge
            String icaoCode = "FGI";
            doThrow(new AirlineNotFoundException(icaoCode)).when(airlineService).findByAirline(any());

            //act e assert
            mockMvc.perform(get("/api/airlines/" + icaoCode))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + icaoCode + " not found."));

        }

    }

    @Nested
    @DisplayName("PUT /api/airlines/{name}")
    class updateAirlinesExist {

        @Test
        @DisplayName("Deve atualizar a companhia aérea e retorna o status 200 - Ok")
        void shouldUpdateAirlineSuccessfully() throws Exception {
            //arrange
            String icaoCode = "FGI";
            AirlineDtoRequest request = new AirlineDtoRequest("FlyGold Airlines", "FGI");
            AirlineDtoResponse response = new AirlineDtoResponse("FlyGold Airlines", "FGI");
            when(airlineService.editAirline(icaoCode, request)).thenReturn(response);

            //act e assert
            mockMvc.perform(put("/api/airlines/" + icaoCode)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("FlyGold Airlines"))
                    .andExpect(jsonPath("$.icaoCode").value("FGI"));
        }

        @Test
        @DisplayName("Deve lançar a exceção AirlineNotFoundException quando a companhia aérea não existir.")
        void shouldThrowAirlineNotFoundExceptionWhenDoesNotExist() throws Exception {
            //arrange
            String icaoCode = "FGI";
            AirlineDtoRequest request = new AirlineDtoRequest("FlyGold Airlines", "FGI");

            doThrow(new AirlineNotFoundException(icaoCode)).when(airlineService).editAirline(any(), any());
            //act e assert
            mockMvc.perform(put("/api/airlines/" + icaoCode)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + icaoCode + " not found."));
        }

    }

    @Nested
    @DisplayName("DELETE /api/airlines/{name}")
    class deleteAirlinesExist {


        @Test
        @DisplayName("Deve deletar a companhia existente e retornar status 204")
        void shouldDeleteAirlineWhenExist() throws Exception {
            //
            String icaoCode = "FGI";
            doNothing().when(airlineService).delete(icaoCode);

            //act e assert
            mockMvc.perform(delete("/api/airlines/" + icaoCode))
                    .andExpect(status().isNoContent());

        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar deletar uma companhia aérea que não existe")
        void shouldDeleteAirlineWhenDoesNotExist() throws Exception {
            //
            String icaoCode = "FGI";
            doThrow(new AirlineNotFoundException(icaoCode)).when(airlineService).delete(icaoCode);

            //act e assert
            mockMvc.perform(delete("/api/airlines/" + icaoCode))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Airline with ICAO code " + icaoCode + " not found."));

        }
    }


    @Nested
    @DisplayName("POST /api/airlines/")
    class createNewAirlines {

        @Test
        @DisplayName("Deve criar a companhia aérea e retornar o status 201 CREATED")
        void shouldReturnAirlinesIsCreatedWithSuccess() throws Exception {
            //arrange
            String icaoCode = "FGI";
            AirlineDtoRequest request = new AirlineDtoRequest("FlyGold Airlines", "FGI");
            AirlineDtoResponse response = new AirlineDtoResponse("FlyGold Airlines", "FGI");
            when(airlineService.createAirline(any())).thenReturn(response);

            //act e assert
            mockMvc.perform(post("/api/airlines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(response.name()))
                    .andExpect(jsonPath("$.icaoCode").value(response.icaoCode()));


        }

        @Test
        @DisplayName("Deve retornar o erro 422 - UNPROCESS ENTITY  quando tentar cadastrar uma companhia aérea com o icaoCode já existente.")
        void shouldThrowAirlineAlreadyExceptionWhenAlreadyExists() throws Exception {
            //arrange
            String icaoCode = "FGI";
            AirlineDtoRequest request = new AirlineDtoRequest("FlyGold Airlines", "FGI");
            doThrow(new AirlineAlreadyExist(icaoCode)).when(airlineService).createAirline(any());

            //act e assert
            mockMvc.perform(post("/api/airlines")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string("Airline with icao code: " + icaoCode + " already exist."));

        }

    }


}