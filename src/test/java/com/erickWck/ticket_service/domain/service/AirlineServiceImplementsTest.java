package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.airline.AirlineDtoRequest;
import com.erickWck.ticket_service.domain.entity.Airline;
import com.erickWck.ticket_service.domain.exception.AirlineAlreadyExist;
import com.erickWck.ticket_service.domain.exception.AirlineNotFoundException;
import com.erickWck.ticket_service.domain.repository.AirlineRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirlineServiceImplementsTest {

    @Mock
    private AirlineRepository airlineRepository;

    @InjectMocks
    private AirlineServiceImplements airlineService;

    @Captor
    private ArgumentCaptor<Airline> captor;

    @Nested
    class findAllAirline {

        private Airline airline;

        @BeforeEach
        void setup() {
            airline = new Airline(1L, "Fly Cell", "FLC",null,null,0);
        }

        @Test
        @DisplayName("Lista as empresas aéreas cadastrada.")
        void shouldListWhenAirlineExists() {
            //arrange
            List<Airline> list = new ArrayList<>();
            Airline airline2 = new Airline(1L, "Airbus One", "ABO",null,null,0);
            list.add(airline);
            list.add(airline2);
            when(airlineRepository.findAll()).thenReturn(list);

            //act
            var resultList = airlineService.findAllAirline();

            //assert
            assertEquals(2, list.size());
            assertEquals(airline.getName(), resultList.get(0).name());
            assertEquals(airline.getIcaoCode(), resultList.get(0).icaoCode());
            assertEquals(airline2.getName(), resultList.get(1).name());
            assertEquals(airline2.getIcaoCode(), resultList.get(1).icaoCode());

        }

        @Test
        @DisplayName("Lista as empresas aéreas cadastrada, mais o retorno é empty/vazio.")
        void shouldReturnEmptyList() {
            //arrange
            List<Airline> list = new ArrayList<>();
            when(airlineRepository.findAll()).thenReturn(list);

            //act
            var resultList = airlineService.findAllAirline();

            //assert
            assertEquals(0, list.size());
            verify(airlineRepository, times(1)).findAll();
        }

    }

    @Nested
    class findByAirline {

        private Airline airline;

        @BeforeEach
        void setup() {
            airline = new Airline(1L, "Fly Cell", "FLC",null,null,0);
        }

        @Test
        void shouldReturnAirlineWhenExists() {
            //arrange
            String iacao = "FLC";
            when(airlineRepository.findByIcaoCode(iacao)).thenReturn(Optional.of(airline));

            //act
            var result = airlineService.findByAirline(iacao);

            assertNotNull(result);
            assertEquals(airline.getName(), result.name());
            assertEquals(airline.getIcaoCode(), result.icaoCode());
        }

        @Test
        @DisplayName("Lança a exceção AirlineNotFoundException quando não existe companhia aérea.")
        void shouldThrowAirlineNotFoundExceptionWhenDoesNotExist() {
            //arrange
            String iacao = "FLC";
            when(airlineRepository.findByIcaoCode(iacao)).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AirlineNotFoundException.class, () -> {
                airlineService.findByAirline(iacao);
            });

            //assert
            String message = "Airline with ICAO code " + iacao + " not found.";
            assertEquals(message, messageException.getMessage());
        }


    }

    @Nested
    class delete {

        private Airline airline;

        @BeforeEach
        void setup() {
            airline = new Airline(1L, "Fly Cell", "FLC",null,null,0);
        }

        @Test
        @DisplayName("Deve excluir a companhia aérea quando existe.")
        void shouldDeleteAirlineWhenExist() {
            //arrange
            String icao = "FLC";
            when(airlineRepository.findByIcaoCode(icao)).thenReturn(Optional.of(airline));

            //act e assert
            airlineService.delete(icao);
            verify(airlineRepository, times(1)).deleteByIcaoCode(icao);
        }

        @Test
        @DisplayName("Deve lançar a exceção quando não encontra a companhia aérea para excluir.")
        void shouldThrowAirlineNotFoundExceptionWhenDoesNotExists() {
            //arrange
            String icao = "";
            when(airlineRepository.findByIcaoCode(icao)).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AirlineNotFoundException.class, () -> {
                airlineService.delete(icao);
            });

            //assert
            String message = "Airline with ICAO code " + icao + " not found.";
            assertEquals(message, messageException.getMessage());
        }
    }

    @Nested
    class editAirline {

        private Airline airline;

        @BeforeEach
        void setup() {
            airline = new Airline(1L, "Fly Cell", "FLC",null,null,0);
        }

        @Test
        @DisplayName("Deve atualizar a companhia aérea.")
        void shouldReturnAirlineUpdateWhenExists() {
            //arrange
            String icao = "FLC";

            AirlineDtoRequest request = new AirlineDtoRequest("Fly Emirates", "FLC");

            Airline airlineUpdate = new Airline(airline.getId(), request.name(), request.icaoCode(),null,null,0);
            when(airlineRepository.findByIcaoCode(icao)).thenReturn(Optional.of(airlineUpdate));
            when(airlineRepository.save(any(Airline.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //act
            var result = airlineService.editAirline(icao, request);

            //assert
            assertNotNull(result);
            assertEquals(airlineUpdate.getName(), result.name());
            assertEquals(airlineUpdate.getIcaoCode(), result.icaoCode());
        }


        @Test
        @DisplayName("Deve lançar a exceção quando não encontra companhia aérea para atualizar.")
        void shouldThrowAirlineNotFoundExceptionWhenDoesNotExist() {
            //arrange
            String icao = "FLC";
            AirlineDtoRequest request = new AirlineDtoRequest("Fly Emirates", "FLC");
            when(airlineRepository.findByIcaoCode(icao)).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AirlineNotFoundException.class, () -> {
                airlineService.editAirline(icao, request);
            });
            String message = "Airline with ICAO code " + icao + " not found.";
            assertEquals(message, messageException.getMessage());

        }
    }

    @Nested
    class createAirline {


        @Test
        @DisplayName("Deve cirar a companhia aérea quando não existe.")
        void shouldCreateAirlineWhenDoesNotExist() {
            //arrange
            var request = new AirlineDtoRequest("Fly Cell", "FLC");
            when(airlineRepository.save(any(Airline.class))).thenAnswer(invocation -> invocation.getArgument(0));


            //act
            var resultCreate = airlineService.createAirline(request);

            //assert
            verify(airlineRepository, times(1)).save(captor.capture());
            var captured = captor.getValue();
            assertEquals(captured.getIcaoCode(), resultCreate.icaoCode());
            assertEquals(captured.getName(), resultCreate.name());

        }

        @Test
        @DisplayName("Deve lançar a exceção, se já existi a companhia aérea com o icaoCode")
        void shouldThrowAirlineAlreadyExceptionWhenExist() {
            //arrange
            String icao = "FLC";
            var request = new AirlineDtoRequest("Fly Cell", "FLC");
            var airline = new Airline(1L, "Fly Cell", icao,null,null,0);
            when(airlineRepository.findByIcaoCode(icao)).thenReturn(Optional.of(airline));

            //act

            var messageException = assertThrows(AirlineAlreadyExist.class, () -> {
                airlineService.createAirline(request);
            });

            String message = "Airline with icao code: " + icao + " already exist.";
            assertEquals(message, messageException.getMessage());
        }

    }
}