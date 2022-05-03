package com.example.javaintern.domains.cargo.utils;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class TotalCargo {

    private int flightId;

    @JsonProperty("baggage")
    private Set<Baggage> baggages;

    @JsonProperty("cargo")
    private Set<AirplaneCargo> airplaneCargos;
}
