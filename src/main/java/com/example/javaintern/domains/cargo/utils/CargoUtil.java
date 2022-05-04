package com.example.javaintern.domains.cargo.utils;

import com.example.javaintern.domains.flight.Flight;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@ToString
@MappedSuperclass
public abstract class CargoUtil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @NotNull
    @Min(value = 1, message = "Weight must be greater then zero.")
    private int weight;

    @NotBlank
    @Pattern(regexp = "(lb|kg)", message = "Wrong weight unit, available are kg and lb.")
    private String weightUnit;

    @NotNull
    @Min(value = 1, message = "Number of pieces must be greater then zero.")
    private int pieces;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    public CargoUtil(int weight, String weightUnit, int pieces) {
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.pieces = pieces;
    }
}
