package com.example.javaintern.domains.cargo.utils;

import com.example.javaintern.domains.flight.Flight;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@MappedSuperclass
abstract class CargoUtil {
    @Id
    private int id;

    @NotNull
    private int weight;

    @NotNull
    private String weightUnit;

    @NotNull
    private int pieces;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public CargoUtil(int weight, String weightUnit, int pieces, Flight flight) {
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.pieces = pieces;
        this.flight = flight;
    }
}
