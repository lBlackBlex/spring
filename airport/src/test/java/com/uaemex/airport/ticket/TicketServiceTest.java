package com.uaemex.airport.ticket;

import com.uaemex.airport.route.Route;
import com.uaemex.airport.route.RouteRepository;
import com.uaemex.airport.user.User;
import com.uaemex.airport.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RouteRepository routeRepository;
    private TicketController ticketController;

    @BeforeEach
    void setUp() {
        TicketService ticketService = new TicketService(ticketRepository, userRepository, routeRepository);
        ticketController = new TicketController(ticketService);
    }

    @Test
    void getTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());

        when(ticketRepository.findById(ticket.getId()))
                .thenReturn(Optional.of(ticket));
        ticketController.getTicket(ticket.getId());

        verify(ticketRepository).findById(ticket.getId());
    }

    @Test
    void getTicketThrowException() {
        UUID uuid = UUID.randomUUID();

        assertThatThrownBy(() -> ticketController.getTicket(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ticket with id " + uuid + " does not exist");
    }

    @Test
    void getTickets() {
        ticketController.getTickets();
        verify(ticketRepository).findAll();
    }

    @Test
    void getUserTickets() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        user.setTickets(List.of(ticket));

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(ticket.getUser()))
                .thenReturn(List.of(ticket));
        List<Ticket> userTickets = ticketController.getUserTickets(user.getId());

        assertEquals(userTickets.size(), user.getTickets().size());
    }

    @Test
    void getUserTicketsEmpty() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setTickets(new ArrayList<>());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        when(ticketRepository.findAllByUser(user))
                .thenReturn(new ArrayList<>());
        List<Ticket> userTickets = ticketController.getUserTickets(user.getId());

        assertEquals(userTickets.size(), user.getTickets().size());
    }

    @Test
    void addNewTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());

        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        ticketController.registerNewTicket(ticket);

        verify(ticketRepository).save(ticketArgumentCaptor.capture());
        Ticket capturedTicket = ticketArgumentCaptor.getValue();
        assertEquals(capturedTicket, ticket);
    }

    @Test
    void updateTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setSeat("AAA1");

        when(ticketRepository.findById(ticket.getId()))
                .thenReturn(Optional.of(ticket));
        ticketController.updateTicket(
                ticket.getId(),
                true,
                "BBB1",
                true
        );
        Ticket ticketUpdated = ticketController.getTicket(ticket.getId());

        assertTrue(ticketUpdated.isCheck_in());
        assertEquals(ticketUpdated.getSeat(), "BBB1");
        assertTrue(ticketUpdated.isResale());
    }

    @Test
    void addTicketUser() {
        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        Route route = new Route();
        route.setId(UUID.randomUUID());
        route.setTickets(new ArrayList<>());
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setTickets(new ArrayList<>());

        when(ticketRepository.findById(ticket.getId()))
                .thenReturn(Optional.of(ticket));
        when(routeRepository.findById(route.getId()))
                .thenReturn(Optional.of(route));
        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        ticketController.addTicketUser(
                ticket.getId(),
                route.getId(),
                user.getId());

        verify(ticketRepository).findById(ticket.getId());
        verify(routeRepository).findById(route.getId());
        verify(userRepository).findById(user.getId());

        assertNotEquals(ticket.getRoute(), null);
        assertNotEquals(user.getTickets().size(), 0);
        assertNotEquals(route.getTickets().size(), 0);
    }

    @Test
    void deleteTicket() {
        UUID uuid = UUID.randomUUID();

        when(ticketRepository.existsById(uuid))
                .thenReturn(true);
        ticketController.deleteTicket(uuid);

        verify(ticketRepository).deleteById(uuid);
    }

    @Test
    void deleteTicketThrowException(){
        UUID uuid = UUID.randomUUID();
        assertThatThrownBy(() -> ticketController.deleteTicket(uuid))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ticket with id " + uuid + " does not exist");
    }
}