package com.uaemex.airport.terminal;

import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.boardingRoom.BoardingRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TerminalServiceTest {

    @Mock
    private TerminalRepository terminalRepository;
    @Mock
    private BoardingRoomRepository boardingRoomRepository;
    private TerminalController terminalController;

    @BeforeEach
    void setUp() {
        TerminalService terminalService = new TerminalService(terminalRepository, boardingRoomRepository);
        terminalController = new TerminalController(terminalService);
    }

    @Test
    void getTerminal() {
        Terminal terminal = new Terminal();
        terminal.setId(UUID.randomUUID());

        when(terminalRepository.findById(terminal.getId()))
                .thenReturn(Optional.of(terminal));
        terminalController.getTerminal(terminal.getId());

        verify(terminalRepository).findById(terminal.getId());
    }

    @Test
    void getTerminalThrowException(){
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> terminalController.getTerminal(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Terminal with id " + uuid + " does not exist");
    }

    @Test
    void getTerminals() {
        terminalController.getTerminals();
        verify(terminalRepository).findAll();
    }

    @Test
    void addNewTerminal() {
        Terminal terminal = new Terminal();
        terminal.setId(UUID.randomUUID());

        ArgumentCaptor<Terminal> terminalArgumentCaptor = ArgumentCaptor.forClass(Terminal.class);
        terminalController.registerNewTerminal(terminal);

        verify(terminalRepository).save(terminalArgumentCaptor.capture());
        Terminal capturedTerminal = terminalArgumentCaptor.getValue();
        assertEquals(capturedTerminal, terminal);

    }

    @Test
    void addTerminalBoardingRoom() {
        Terminal terminal = new Terminal();
        terminal.setId(UUID.randomUUID());
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());
        boardingRoom.setTerminal(terminal);

        when(terminalRepository.findById(terminal.getId()))
                .thenReturn(Optional.of(terminal));
        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        terminalController.addTerminalBoardingRoom(
                terminal.getId(),
                boardingRoom.getId());

        verify(terminalRepository).findById(terminal.getId());
        verify(boardingRoomRepository).findById(boardingRoom.getId());
        assertNotEquals(boardingRoom.getTerminal(), null);
        assertNotEquals(terminal.getBoardingRooms().size(), 0);
    }

    @Test
    void removeTerminalBoardingRoom() {
        Terminal terminal = new Terminal();
        terminal.setId(UUID.randomUUID());
        BoardingRoom boardingRoom = new BoardingRoom();
        boardingRoom.setId(UUID.randomUUID());

        when(terminalRepository.findById(terminal.getId()))
                .thenReturn(Optional.of(terminal));
        when(boardingRoomRepository.findById(boardingRoom.getId()))
                .thenReturn(Optional.of(boardingRoom));
        terminalController.removeTerminalBoardingRoom(
                terminal.getId(),
                boardingRoom.getId());

        verify(terminalRepository).findById(terminal.getId());
        verify(boardingRoomRepository).findById(boardingRoom.getId());
        assertEquals(terminal.getBoardingRooms().size(), 0);
    }

    @Test
    void deleteTerminal(){
        UUID uuid = UUID.randomUUID();

        when(terminalRepository.existsById(uuid))
                .thenReturn(true);
        terminalController.deleteTerminal(uuid);

        verify(terminalRepository).deleteById(uuid);
    }

    @Test
    void deleteTerminalThrowException() {
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> terminalController.deleteTerminal(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Terminal with id " + uuid + " does not exist");
    }
}
