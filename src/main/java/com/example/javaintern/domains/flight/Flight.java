package com.example.javaintern.domains.flight;

import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.OffsetDateTime;
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
    @Min(value = 1, message = "Flight number must be greater then zero.")
    private int flightNumber;

    @NotBlank(message = "Arrival code shouldn't be blank.")
    @Pattern(regexp = "[A-Z]{3}", message = "Departure code must contain only three capital letters.")
    private String departureAirportIATACode;

    @NotBlank(message = "Arrival code shouldn't be blank.")
    @Pattern(regexp = "[A-Z]{3}", message = "Arrival code must contain only three capital letters.")
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

    public Flight(int flightNumber, String departureAirportIATACode, String arrivalAirportIATACode, OffsetDateTime departureDate) {
        this.flightNumber = flightNumber;
        this.departureAirportIATACode = departureAirportIATACode;
        this.arrivalAirportIATACode = arrivalAirportIATACode;
        this.departureDate = departureDate;
    }
}
