package com.erickWck.ticket_service.domain.service;

import com.erickWck.ticket_service.domain.entity.Aircraft;
import com.erickWck.ticket_service.domain.repository.AircraftRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceImplementsTest {

    @Mock
    private AircraftRepository aircraftRepository;

    @InjectMocks
    private AircraftServiceImplements aircraftService;

    @Nested
    class findAll {

        @Test
        @DisplayName("Listagem de todos aviões cadastrado.")
        void shouldListAllAircraft() {
            //arrange
            var aircraft = new Aircraft(1L, "Air ceb", 150);
            var aircraft2 = new Aircraft(2L, "Boeing falk", 150);
            var list = new ArrayList<>(List.of(aircraft, aircraft2));
            when(aircraftRepository.findAll()).thenReturn(list);

            //act
            var result = aircraftService.findAll();

            //assert
            assertEquals(2, result.size());
            assertEquals(aircraft.model(), result.get(0).model());
            assertEquals(aircraft.seatCapacity(), result.get(0).seatCapacity());
            assertEquals(aircraft2.model(), result.get(1).model());
            assertEquals(aircraft2.seatCapacity(), result.get(1).seatCapacity());

        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver aviões cadastrados")
        void shouldReturnEmptyListWhenNoAircraftsExist() {
            //arrange
            when(aircraftRepository.findAll()).thenReturn(Collections.emptyList());

            //act
            var result = aircraftService.findAll();

            //assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(aircraftRepository, times(1)).findAll();

        }
    }



}