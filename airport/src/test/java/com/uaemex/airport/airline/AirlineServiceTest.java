package com.uaemex.airport.airline;

import com.uaemex.airport.route.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private AirlineRepository airlineRepository;
    @Mock
    private RouteRepository routeRepository;
    private AirlineController airlineController;

    @BeforeEach
    void setUp() {
        AirlineService airlineService = new AirlineService(airlineRepository, routeRepository);
        airlineController = new AirlineController(airlineService);
    }

    @Test
    void getAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());

        when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        airlineController.getAirline(airline.getId());

        verify(airlineRepository).findById(airline.getId());
    }

    @Test
    void getAirlineThrowException(){
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> airlineController.getAirline(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Airline with id " + uuid + " does not exist");
    }

    @Test
    void getAirlines() {
        airlineController.getAirlines();
        verify(airlineRepository).findAll();
    }

    @Test
    void addNewAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());

        ArgumentCaptor<Airline> airlineArgumentCaptor = ArgumentCaptor.forClass(Airline.class);
        airlineController.createNewAirline(airline);

        verify(airlineRepository).save(airlineArgumentCaptor.capture());
        Airline capturedAirline = airlineArgumentCaptor.getValue();
        assertEquals(capturedAirline, airline);
    }

    @Test
    void updateAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());

        when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        airlineController.updateAirline(
                airline.getId(),
                "new name");
        Airline airlineUpdated = airlineController.getAirline(airline.getId());

        assertEquals(airlineUpdated.getName(), "new name");
    }

    @Test
    void deleteAirline() {
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());

        when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        airlineController.deleteAirline(airline.getId());

        verify(airlineRepository).deleteById(airline.getId());
    }

    @Test
    void deleteAirlineException(){
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> airlineController.deleteAirline(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Airline with id " + uuid + " does not exist");
    }
}