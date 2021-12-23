package com.uaemex.airport.route;

import com.fasterxml.jackson.annotation.*;
import com.uaemex.airport.airline.Airline;
import com.uaemex.airport.boardingRoom.BoardingRoom;
import com.uaemex.airport.plane.Plane;
import com.uaemex.airport.ticket.Ticket;
 import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, updatable = false, name = "date_of_flight")
    private Date date;
    @Column(nullable = false, length = 45, name = "place_of_departure")
    private String from;
    @Column(nullable = false, length = 45, name = "place_of_arrival")
    private String to;
    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp etd;
    @Column(nullable = false, columnDefinition = "timestamp")
    private Timestamp eta;
    @Column(nullable = false)
    private float cost;
    @JsonIgnoreProperties("route")
    @OneToMany
    private List<Ticket> tickets;
    @JsonIgnoreProperties("routes")
    @ManyToMany
    private Set<BoardingRoom> boardingRooms;
    @JsonIgnoreProperties("routes")
    @ManyToMany
    @JoinTable(
            name = "airline_route",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "airline_id")
    )
    private Set<Airline> airlines = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

}
