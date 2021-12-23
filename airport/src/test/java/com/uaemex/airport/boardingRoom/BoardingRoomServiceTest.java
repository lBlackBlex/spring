package com.uaemex.airport.boardingRoom;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardingRoomServiceTest {

    @Mock
    private BoardingRoomRepository boardingRoomRepository;
    @Mock
    private RouteRepository routeRepository;
    private BoardingRoomController boardingRoomController;

    @BeforeEach
    void setUp() {
        BoardingRoomService boardingRoomService = new BoardingRoomService(boardingRoomRepository, routeRepository);
        boardingRoomController = new BoardingRoomController(boardingRoomService);
    }

    @Test
    void getBoardingRoom() {
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());

        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        boardingRoomController.getBoardingRoom(boardingRoom.getId());

        verify(boardingRoomRepository).findById(boardingRoom.getId());
    }

    @Test
    void getRouteThrowException(){
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> boardingRoomController.getBoardingRoom(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Boarding room with id " + uuid + " does not exist");
    }

    @Test
    void getBoardingRooms() {
        boardingRoomController.getBoardingRooms();
        verify(boardingRoomRepository).findAll();
    }

    @Test
    void createBoardingRoom() {
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());

        ArgumentCaptor<BoardingRoom> boardingRoomArgumentCaptor = ArgumentCaptor.forClass(BoardingRoom.class);
        boardingRoomController.createBoardingRoom(boardingRoom);

        verify(boardingRoomRepository).save(boardingRoomArgumentCaptor.capture());
        BoardingRoom capturedBoardingRoom = boardingRoomArgumentCaptor.getValue();
        assertEquals(capturedBoardingRoom, boardingRoom);
    }

    @Test
    void updateBoardingRoom() {
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());

        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        boardingRoomController.updateBoardingRoom(
                boardingRoom.getId(),
                29,
                "new");
        BoardingRoom boardingRoomUpdated = boardingRoomController.getBoardingRoom(boardingRoom.getId());

        assertEquals(boardingRoomUpdated.getCapacity(), 29);
        assertEquals(boardingRoomUpdated.getName(), "new");
    }

    @Test
    void addBoardingRoomRoute() {
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());
        Route route = new Route();
        route.setId(UUID.randomUUID());
        route.setBoardingRooms(new HashSet<>());

        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        boardingRoomController.addBoardingRoomRoute(boardingRoom.getId(), route.getId());

        verify(boardingRoomRepository).findById(boardingRoom.getId());
        verify(routeRepository).findById(route.getId());
        assertNotEquals(boardingRoom.getRoutes().size(), 0);
        assertNotEquals(route.getBoardingRooms().size(), 0);
    }

    @Test
    void removeBoardingRoomRoute() {
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());
        Route route = new Route();
        route.setId(UUID.randomUUID());
        route.setBoardingRooms(new HashSet<>());

        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        boardingRoomController.removeBoardingRoomRoute(boardingRoom.getId(), route.getId());

        verify(routeRepository).findById(route.getId());
        verify(boardingRoomRepository).findById(boardingRoom.getId());
        assertEquals(route.getBoardingRooms().size(), 0);
        assertEquals(boardingRoom.getRoutes().size(), 0);
    }

    @Test
    void deleteBoardingRoom() {
        UUID uuid = UUID.randomUUID();

        when(boardingRoomRepository.existsById(uuid))
                .thenReturn(true);
        boardingRoomController.deleteBoardingRoom(uuid);

        verify(boardingRoomRepository).deleteById(uuid);
    }

    @Test
    void deleteRouteThrowException(){
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> boardingRoomController.deleteBoardingRoom(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Boarding room with id " + uuid + " does not exist");
    }
}