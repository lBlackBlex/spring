package com.uaemex.airport.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.uaemex.airport.plane.Plane;
import com.uaemex.airport.ticket.Ticket;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
)
public class User {
    @Id
    @GeneratedValue
    @Column(updatable = false, columnDefinition = "binary(16)")
    private UUID id;
    @Column(nullable = false, length = 45)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 45)
    private String name;
    @Column(nullable = false, length = 45)
    private String last_name;
    @Column(nullable = false, columnDefinition = "varchar(10) default 'USER'")
    private String role = "USER";
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Ticket> tickets;
    @JsonIgnoreProperties("pilots")
    @ManyToMany
    @JoinTable(
            name = "plane_pilot",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "plane_id")}
    )
    private Set<Plane> planes = new HashSet<>();
}
