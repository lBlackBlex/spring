package com.uaemex.airport.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uaemex.airport.route.Route;
import com.uaemex.airport.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Ticket {

    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean check_in = false;
    @Column(nullable = false, columnDefinition = "char(5)")
    private String seat;
    @Column(nullable = false, columnDefinition = "bit default 0")
    private boolean resale = false;
    @JsonIgnoreProperties("tickets")
    @ManyToOne()
    @JoinColumn(name = "route_id")
    private Route route;
    @JsonIgnoreProperties("tickets")
    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
