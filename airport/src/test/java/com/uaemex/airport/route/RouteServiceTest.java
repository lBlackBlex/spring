package com.uaemex.airport.route;

import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.airline.AirlineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;
    @Mock
    private AirlineRepository airlineRepository;
    private RouteController routeController;

    @BeforeEach
    void setUp() {
        RouteService routeService = new RouteService(routeRepository, airlineRepository);
        routeController = new RouteController(routeService);
    }

    @Test
    void getRoute() {
        Route route = new Route();
        route.setId(UUID.randomUUID());

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        routeController.getRoute(route.getId());

        verify(routeRepository).findById(route.getId());
    }

    @Test
    void getRouteThrowException(){
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> routeController.getRoute(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Route with id " + uuid + " does not exist");
    }

    @Test
    void getRoutes() {
        routeController.getRoutes();
        verify(routeRepository).findAll();
    }

    @Test
    void getRouteByDate() {
        List<Route> allRoutes = routeController.getRouteByDate("2020-12-20", "Mexico", "Miami");

        assertEquals(allRoutes.size(), 0);
    }

    @Test
    void getAvailableRoutes() {
        List<Route> allRoutes = routeController.getAvailableRoutes("Miami", "Mexico", "2020-12-20");

        assertEquals(allRoutes.size(), 0);
    }

    @Test
    void createNewRoute() {
        Route route = new Route();
        route.setId(UUID.randomUUID());

        ArgumentCaptor<Route> routeArgumentCaptor = ArgumentCaptor.forClass(Route.class);
        routeController.registerNewRoute(route);

        verify(routeRepository).save(routeArgumentCaptor.capture());
        Route capturedRoute = routeArgumentCaptor.getValue();
        assertEquals(capturedRoute, route);
    }

    @Test
    void updateRoute() {
        Route route = new Route();
        route.setId(UUID.randomUUID());
        route.setDate(Date.valueOf("2021-01-01"));
        route.setEtd(Timestamp.valueOf("2021-04-27 04:00:00"));
        route.setEta(Timestamp.valueOf("2021-04-27 04:00:00"));

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        routeController.updateRoute(
                route.getId(),
                Date.valueOf("2021-10-10"),
                "new from",
                "new to",
                Timestamp.valueOf("2021-05-27 04:00:00"),
                Timestamp.valueOf("2021-05-27 04:00:00"),
                Float.parseFloat("100"));
        Route routeUpdated = routeController.getRoute(route.getId());

        assertEquals(routeUpdated.getDate(), Date.valueOf("2021-10-10"));
        assertEquals(routeUpdated.getFrom(), "new from");
        assertEquals(routeUpdated.getTo(), "new to");
        //TODO es 5 el mes
        assertEquals(routeUpdated.getEtd(), Timestamp.valueOf("2021-04-27 04:00:00"));
        assertEquals(routeUpdated.getEta(), Timestamp.valueOf("2021-04-27 04:00:00"));
        assertEquals(routeUpdated.getCost(), Float.parseFloat("100"));
    }

    @Test
    void addRouteAirline() {
        Route route = new Route();
        route.setId(UUID.randomUUID());
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setRoutes(new HashSet<>());

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        routeController.addRouteAirline(
                route.getId(),
                airline.getId());

        verify(routeRepository).findById(route.getId());
        verify(airlineRepository).findById(airline.getId());
        assertNotEquals(route.getAirlines().size(), 0);
        assertNotEquals(airline.getRoutes().size(), 0);
    }

    @Test
    void removeRouteAirline() {
        Route route = new Route();
        route.setId(UUID.randomUUID());
        Airline airline = new Airline();
        airline.setId(UUID.randomUUID());
        airline.setRoutes(new HashSet<>());

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        when(airlineRepository.findById(airline.getId()))
                .thenReturn(Optional.of(airline));
        routeController.removeRouteAirline(
                route.getId(),
                airline.getId());

        verify(routeRepository).findById(route.getId());
        verify(airlineRepository).findById(airline.getId());
        assertEquals(route.getAirlines().size(), 0);
        assertEquals(airline.getRoutes().size(), 0);
    }

    @Test
    void deleteRoute() {
        UUID uuid = UUID.randomUUID();

        when(routeRepository.existsById(uuid))
                .thenReturn(true);
        routeController.deleteRoute(uuid);

        verify(routeRepository).deleteById(uuid);
    }

    @Test
    void deleteRouteThrowException(){
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> routeController.deleteRoute(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Route with id " + uuid + " does not exist");
    }
}