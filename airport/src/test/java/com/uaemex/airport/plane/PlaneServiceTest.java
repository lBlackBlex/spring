package com.uaemex.airport.plane;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import com.uaemex.airport.user.UserRepository;
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
class PlaneServiceTest {

    @Mock
    private PlaneRepository planeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RouteRepository routeRepository;
    private PlaneController planeController;

    @BeforeEach
    void setUp() {
        PlaneService planeService = new PlaneService(planeRepository, userRepository, routeRepository);
        planeController = new PlaneController(planeService);
    }

    @Test
    void getPlane() {
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());

        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        planeController.getPlane(plane.getId());

        verify(planeRepository).findById(plane.getId());
    }

    @Test
    void getPlaneThrownException(){
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> planeController.getPlane(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Plane with id " + uuid + " does not exist");
    }

    @Test
    void getPlanes() {
        planeController.getPlanes();
        verify(planeRepository).findAll();
    }

    @Test
    void addNewPlane() {
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());

        ArgumentCaptor<Plane> planeArgumentCaptor = ArgumentCaptor.forClass(Plane.class);
        planeController.registerNewPlane(plane);

        verify(planeRepository).save(planeArgumentCaptor.capture());
        Plane capturedPlane = planeArgumentCaptor.getValue();
        assertEquals(capturedPlane, plane);
    }

    @Test
    void updatePlane() {
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());

        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        planeController.updatePlane(
                plane.getId(),
                200,
                "AAA1",
                "XXX2323");
        Plane planeUpdated = planeController.getPlane(plane.getId());

        assertEquals(planeUpdated.getCapacity(), 200);
        assertEquals(planeUpdated.getModel(), "AAA1");
        assertEquals(planeUpdated.getAircraft_code(), "XXX2323");
    }

    @Test
    void addPlaneRoute() {
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());
        Route route = new Route();
        route.setId(UUID.randomUUID());

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        planeController.addPlaneRoute(plane.getId(), route.getId());

        verify(routeRepository).findById(route.getId());
        verify(planeRepository).findById(plane.getId());
        assertNotEquals(route.getPlane(), null);
        assertNotEquals(plane.getRoutes().size(), 0);
    }

    @Test
    void removePlaneRoute() {
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());
        Route route = new Route();
        route.setId(UUID.randomUUID());

        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        planeController.removePlaneRoute(plane.getId(), route.getId());

        verify(routeRepository).findById(route.getId());
        verify(planeRepository).findById(plane.getId());
        assertEquals(route.getAirlines().size(), 0);
        assertEquals(plane.getRoutes().size(), 0);
    }

    @Test
    void deletePlane(){
        Plane plane = new Plane();
        plane.setId(UUID.randomUUID());

        when(planeRepository.findById(plane.getId()))
                .thenReturn(Optional.of(plane));
        planeController.deletePlane(plane.getId());

        verify(planeRepository).deleteById(plane.getId());
    }

    @Test
    void deletePlaneThrowException() {
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> planeController.deletePlane(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Plane with id " + uuid + " does not exist");
    }
}