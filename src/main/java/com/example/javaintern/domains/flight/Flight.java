package com.example.javaintern.domains.flight;

import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int flightId;

    @NotNull
    private int flightNumber;

    @NotNull
    private String departureAirportIATACode;

    @NotNull
    private String arrivalAirportIATACode;

    private OffsetDateTime departureDate;

    @JsonIgnore
    @OneToMany(mappedBy = "flight",
               cascade = CascadeType.ALL)
    private Set<AirplaneCargo> airplaneCargos;

    @JsonIgnore
    @OneToMany(
            mappedBy = "flight",
            cascade = CascadeType.ALL)
    private Set<Baggage> baggages;

    public Flight(int flightNumber, String departureAirportIATACode, String arrivalAirportIATACode, OffsetDateTime departureDate, Set<AirplaneCargo> airplaneCargos, Set<Baggage> baggages) {
        this.flightNumber = flightNumber;
        this.departureAirportIATACode = departureAirportIATACode;
        this.arrivalAirportIATACode = arrivalAirportIATACode;
        this.departureDate = departureDate;
        this.airplaneCargos = airplaneCargos;
        this.baggages = baggages;
    }
}
