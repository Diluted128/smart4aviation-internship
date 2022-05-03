package com.example.javaintern.domains.flight.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeightSummary {
    private int baggageWeight;
    private int cargoWeight;
    private int totalWeight;
}
