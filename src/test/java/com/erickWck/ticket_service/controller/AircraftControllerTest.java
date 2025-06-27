package com.erickWck.ticket_service.controller;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoResponse;
import com.erickWck.ticket_service.domain.exception.AircraftAlreadyException;
import com.erickWck.ticket_service.domain.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.domain.service.contract.AircraftService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftController.class)
class AircraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AircraftService aircraftService;

    @Nested
    @DisplayName("GET /api/aircrafts")
    class ListAircrafts {
        @Test
        @DisplayName("Deve retornar todos os aviões com status 200")
        void shouldReturnLisOfAirCrafts() throws Exception {

            List<AircraftDtoResponse> list = List.of(new AircraftDtoResponse("Boeing 747", "Boeign", 300),
                    new AircraftDtoResponse("Airbus A320", "Boeign", 180));

            when(aircraftService.findAll()).thenReturn(list);

            mockMvc.perform(get("/api/aircrafts"))
                    .andExpect(status().is(200))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(list.size()))
                    .andExpect(jsonPath("$[0].model").value("Boeing 747"))
                    .andExpect(jsonPath("$[1].model").value("Airbus A320"));
        }

    }


    @Nested
    @DisplayName("GET /api/aircrafts/{model}")
    class GetAircraftByModel {

        @Test
        @DisplayName("Deve retornar o avião pelo model com status 200")
        void shouldReturnAircraftWhenExists() throws Exception {
            String model = "Airbus A320";
            var dtoResponse = new AircraftDtoResponse("Airbus A320", "Boeign", 180);

            when(aircraftService.findByModelAircraft(model)).thenReturn(dtoResponse);

            mockMvc.perform(get("/api/aircrafts/" + model))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.model").value(dtoResponse.model()))
                    .andExpect(jsonPath("$.manufacturer").value(dtoResponse.manufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(dtoResponse.seatCapacity()));
        }

        @Test
        @DisplayName("Deve lançar erro 404 quando o avião não existe")
        void shouldThrowAircraftNotFoundWhenDoesNotExist() throws Exception {
            // arrange
            String model = "Airbus A320";
            doThrow(AircraftNotFoundException.class).when(aircraftService).findByModelAircraft(model);


            mockMvc.perform(get("/api/aircrafts/" + model))
                    .andExpect(status().is(404));
        }
    }

    @Nested
    @DisplayName("POST /api/aircrafts")
    class createNewAircraft {


        @Test
        @DisplayName("Deve criar um novo avião com sucesso e retornar status 201")
        void shouldCreateAircraft() throws Exception {
            //arrange
            String model = "Airbus A320";
            var dtoResponse = new AircraftDtoResponse("Airbus A320", "Boeign", 180);
            when(aircraftService.createAircraft(any())).thenReturn(dtoResponse);

            mockMvc.perform(post("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "model": "Airbus A320",
                                      "manufacturer": "Airbus",
                                      "seatCapacity": 180
                                    }
                                    """))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.model").value(dtoResponse.model()))
                    .andExpect(jsonPath("$.manufacturer").value(dtoResponse.manufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(dtoResponse.seatCapacity()));
        }

        @Test
        @DisplayName("Deve lançar erro 422(UNPROCESSABLE_ENTITY)  quando o avião já existe")
        void shouldAircraftAlreadyExceptionWhenAircraftExists() throws Exception {
            //arrange
            String model = "Airbus A320";
            var request = new AircraftDtoRequest("Airbus A320", "Boeign", 180);
            doThrow(new AircraftAlreadyException(model)).when(aircraftService).createAircraft(any());

            mockMvc.perform(post("/api/aircrafts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().string("Aircraft with mode: " + model + " already exist."));
        }
    }

    @Nested
    @DisplayName("PUT /api/aircrafts/{model}")
    class UpdateAircraft {

        @Test
        @DisplayName("Deve atualizar um avião existente e retornar status 200")
        void shouldUpdateAircraftSuccessfully() throws Exception {
            //arrange
            String model = "AirbusA320";
            var request = new AircraftDtoRequest("Airbus A320", "Airbus", 186);
            var response = new AircraftDtoResponse("Airbus A320", "Airbus", 186);

            when(aircraftService.editAircraft(model, request)).thenReturn(response);
            //act e assert
            String encodeModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
            mockMvc.perform(put("/api/aircrafts/" + encodeModel)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.model").value(response.model()))
                    .andExpect(jsonPath("$.manufacturer").value(response.manufacturer()))
                    .andExpect(jsonPath("$.seatCapacity").value(response.seatCapacity()));
        }


        @Test
        @DisplayName("Deve retornar 404 ao tentar atualizar um avião que não existe")
        void shouldReturnNotFoundWhenUpdatingNonExistentAircraft() throws Exception {

            //arrange
            String model = "AirbusA320";
            var request = new AircraftDtoRequest("Airbus A320", "Airbus", 186);


            doThrow(new AircraftNotFoundException(model)).when(aircraftService).editAircraft(model, request);
            //act e assert
            String encodeModel = URLEncoder.encode(model, StandardCharsets.UTF_8);
            mockMvc.perform(put("/api/aircrafts/" + encodeModel)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Aircraft with model: " + model + " not found."));
        }

    }


    @Nested
    @DisplayName("DELETE /api/aircrafts/{model}")
    class DeleteAircraft {

        @Test
        @DisplayName("Deve deletar um avião existente e retornar status 204")
        void shouldDeleteAircraftSuccessfully() throws Exception {
            //arrange
            String model = "KC-130";
            String encode = URLEncoder.encode(model, StandardCharsets.UTF_8);

            mockMvc.perform(delete("/api/aircrafts/" + encode))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar deletar um avião que não existe")
        void shouldReturnNotFoundWhenDeletingNonExistentAircraft() throws Exception {
            String model = "KC-130";
            String encode = URLEncoder.encode(model, StandardCharsets.UTF_8);
            doThrow(new AircraftNotFoundException(model)).when(aircraftService).deleteAircraft(any());

            mockMvc.perform(delete("/api/aircrafts/" + encode))
                    .andExpect(status().isNotFound())
                    .andExpect(content().string("Aircraft with model: " + model + " not found."));
        }
    }
}