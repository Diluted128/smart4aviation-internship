package com.example.javaintern.domains.flight.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WeightSummary {
    private double baggageWeightKG;
    private double cargoWeightKG;
    private double totalWeightKG;
}
