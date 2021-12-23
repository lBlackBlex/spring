package com.uaemex.airport.boardingRoom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.terminal.Terminal;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "boarding_room",
        uniqueConstraints = {
                @UniqueConstraint(name = "boarding_room_name_unique", columnNames = "name")
        }
)
public class BoardingRoom {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "smallint")
    private int capacity;
    @Column(nullable = false, length = 45)
    private String name;
    @JsonIgnoreProperties("boardingRooms")
    @ManyToOne
    @JoinColumn(name = "terminal_id")
    private Terminal terminal;
    @JsonIgnoreProperties("boardingRooms")
    @ManyToMany
    @JoinTable(
            name = "boarding_room_route",
            joinColumns = @JoinColumn(name = "boarding_room_id"),
            inverseJoinColumns = @JoinColumn(name = "route_id")
    )
    private Set<Route> routes = new HashSet<>();
}
