package com.example.javaintern.domains.flight;

import com.example.javaintern.domains.cargo.utils.Baggage;
import com.example.javaintern.domains.cargo.utils.CargoInd;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
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
    private String departureAirportATACode;

    @NotNull
    private String arrivalAirportIATACode;

    @NotNull
    private String departureDate;

    @OneToMany(mappedBy = "flight")
    private Set<CargoInd> cargoIndSet;

    @OneToMany(mappedBy = "flight")
    private Set<Baggage> baggages;

    public Flight(int flightNumber, String departureAirportATACode, String arrivalAirportIATACode, String departureDate, Set<CargoInd> cargoIndSet, Set<Baggage> baggages) {
        this.flightNumber = flightNumber;
        this.departureAirportATACode = departureAirportATACode;
        this.arrivalAirportIATACode = arrivalAirportIATACode;
        this.departureDate = departureDate;
        this.cargoIndSet = cargoIndSet;
        this.baggages = baggages;
    }
}
