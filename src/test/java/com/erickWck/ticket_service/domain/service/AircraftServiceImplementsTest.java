package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.controller.dto.aircraft.AircraftDtoRequest;
import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.exception.AircraftAlreadyException;
import com.erickWck.ticket_service.domain.exception.AircraftNotFoundException;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceImplementsTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @Captor
    private ArgumentCaptor<Aircraft> argumentCaptor;

    @InjectMocks
    private AircraftServiceImplements aircraftService;

    @Nested
    class findAll {

        @Test
        @DisplayName("Listagem de todos aviões cadastrado.")
        void shouldListAllAircraft() {
            //arrange
            var aircraft = new Aircraft(1L, "Air ceb", "Airbus", 150,null,null,0);
            var aircraft2 = new Aircraft(2L, "Boeing falk", "Airbus", 150,null,null,0);
            var list = new ArrayList<>(List.of(aircraft, aircraft2));
            when(aircraftRepository.findAll()).thenReturn(list);

            //act
            var result = aircraftService.findAll();

            //assert
            assertEquals(2, result.size());
            assertEquals(aircraft.getModel(), result.get(0).model());
            assertEquals(aircraft.getSeatCapacity(), result.get(0).seatCapacity());
            assertEquals(aircraft2.getModel(), result.get(1).model());
            assertEquals(aircraft2.getSeatCapacity(), result.get(1).seatCapacity());

        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver aviões cadastrados")
        void shouldReturnEmptyListWhenNoAircraftsExist() {
            //arrange
            when(aircraftRepository.findAll())
                    .thenReturn(Collections.emptyList());

            //act
            var result = aircraftService.findAll();

            //assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(aircraftRepository, times(1)).findAll();

        }


    }

    @Nested
    class findByModelAir {

        @Test
        @DisplayName("Deve retornar o avião quando ele existi.")
        void shouldReturnAircraftWhenExist() {
            //arrange
            String model = "Air ceb";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.of(aircraft));

            //act
            var result = aircraftService.findByModelAircraft(model);

            //assert
            assertNotNull(result);
            assertEquals(aircraft.getModel(), result.model());
            assertEquals(aircraft.getSeatCapacity(), result.seatCapacity());
            verify(aircraftRepository, times(1)).findByModel(model);

        }

        @Test
        @DisplayName("Deve lançar AircraftNotFoundException quando o avião já existir.")
        void shouldThrowAircraftNotFoundExceptionWhenDoesNotExist() {
            //arrange
            String model = "Air ceb";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AircraftNotFoundException.class, () -> {
                aircraftService.findByModelAircraft(model);
            });

            //assert
            String message = "Aircraft with model: " + model + " not found.";
            assertEquals(message, messageException.getMessage());
            verify(aircraftRepository, times(1)).findByModel(model);
        }

    }

    @Nested
    class editAircraft {

        @Test
        @DisplayName("Deve editar o avião quando ele existir.")
        void shouldEditAircraftWhenExist() {
            //arrange
            String model = "Air ceb";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            AircraftDtoRequest request = new AircraftDtoRequest(model, "Airbus",189);

            when(aircraftRepository.findByModel(model)).thenReturn(Optional.of(aircraft));
            when(aircraftRepository.save(any(Aircraft.class))).thenAnswer(invocation -> invocation.getArgument(0));

            //act
            var airUpdate = aircraftService.editAircraft(model, request);

            //assert
            verify(aircraftRepository, times(1)).save(argumentCaptor.capture());
            var captured = argumentCaptor.getValue();
            assertEquals(airUpdate.model(), captured.getModel());
            assertEquals(airUpdate.seatCapacity(), captured.getSeatCapacity());
            assertEquals(aircraft.getUuid(), captured.getUuid());
        }


        @Test
        @DisplayName("Deve lançar AircraftNotFoundException quando avião não existir.")
        void shouldThrowAircraftNotFoundExceptionWhenFindModelDoesNotExist() {
            //arrange
            String model = "Air ceb";
            AircraftDtoRequest request = new AircraftDtoRequest(model, "Airbus",189);
            when(aircraftRepository.findByModel(any())).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AircraftNotFoundException.class, () -> {
                aircraftService.editAircraft(model, request);
            });

            //assert
            String message = "Aircraft with model: " + model + " not found.";
            assertEquals(message, messageException.getMessage());
            verify(aircraftRepository, never()).save(argumentCaptor.capture());
        }


    }

    @Nested
    class deleteAircraft {

        @Test
        @DisplayName("Deve excluir o avião quando ele existir.")
        void shouldDeleteAircraftWhenExists() {
            //arrange
            String model = "AERB";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.of(aircraft));
            doNothing().when(aircraftRepository).deleteByModel(model);

            //act
            aircraftService.deleteAircraft(model);

            //act
            verify(aircraftRepository, times(1)).deleteByModel(model);
        }

        @Test
        @DisplayName("Deve lançar AircraftNotFoundException quando avião não existir..")
        void shouldThrowAircraftNotFoundExceptionWhenDeleteDoesNotExist() {
            //arrange
            String model = "AERB";
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.empty());

            //act
            var messageException = assertThrows(AircraftNotFoundException.class, () -> {
                aircraftService.deleteAircraft(model);
            });

            //act
            String message = "Aircraft with model: " + model + " not found.";
            assertEquals(message, messageException.getMessage());
        }
    }

    @Nested
    class createAircraft {

        @Test
        @DisplayName("Deve criar o avião quando ele não existir.")
        void shouldReturnCreatedAircraftWhenDoesNotExist() {
            //arrange
            String model = "Air ceb";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            AircraftDtoRequest request = new AircraftDtoRequest(model, "Airbus",150);
            when(aircraftRepository.save(any(Aircraft.class))).thenReturn(aircraft);

            //act 
            var result = aircraftService.createAircraft(request);

            //assert
            verify(aircraftRepository, times(1)).save(argumentCaptor.capture());
            var captured = argumentCaptor.getValue();
            assertNotNull(captured);
            assertEquals(result.model(), captured.getModel());
            assertEquals(result.seatCapacity(), captured.getSeatCapacity());


        }

        @Test
        @DisplayName("Deve lançar AircraftAlreadyException quando avião existir existir.")
        void shouldThrowAircraftAlreadyExceptionWhenExist() {
            //arrange
            String model = "Air ceb";
            Aircraft aircraft = new Aircraft(1L, model, "Airbus",150,null,null,0);
            AircraftDtoRequest request = new AircraftDtoRequest(model, "Airbus",150);
            when(aircraftRepository.findByModel(model)).thenReturn(Optional.of(aircraft));

            //act

            var messageException = assertThatThrownBy(() -> {
                aircraftService.createAircraft(request);
            })
                    .isInstanceOf(AircraftAlreadyException.class)
                    .hasMessage("Aircraft with model: Air ceb already exist.");

            //assert
            assertNotNull(messageException);

        }
    }

}

