package com.uaemex.airport.airline;

import com.fasterxml.jackson.annotation.*;
import com.uaemex.airport.route.Route;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "airline",
        uniqueConstraints = {
                @UniqueConstraint(name = "airline_unique_name", columnNames = "name")
        }
)
public class Airline {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, length = 45)
    private String name;
    @JsonIgnoreProperties("airlines")
    @ManyToMany(mappedBy = "airlines")
    private Set<Route> routes;
}
