package com.example.javaintern.domains.flight.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AirportSummary {
    private int departingFlights;
    private int arrivingFlights;
    private int quantityOfBaggageDeparting;
    private int quantityOfBaggageArriving;
}
