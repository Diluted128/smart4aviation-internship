package com.example.javaintern.domains.flight.controller;

import com.example.javaintern.domains.flight.Flight;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class FlightController {

    @PostMapping("/flight")
    public void saveFlights(@RequestBody Set<Flight> flight){
        flight.forEach(System.out::println);
    }
}
