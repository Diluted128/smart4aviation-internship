package com.example.javaintern.domains.flight.controller;

import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.controller.util.AirportSummary;
import com.example.javaintern.domains.flight.controller.util.WeightSummary;
import com.example.javaintern.domains.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FlightController {

    private final FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PostMapping("/flight")
    public ResponseEntity<String> saveFlights(@RequestBody List<Flight> flights){
        return flightService.saveFlights(flights);
    }

    @GetMapping("/flight/{number}/weight")
    public ResponseEntity<WeightSummary> getWeightSummary(@PathVariable int number, @RequestParam("date") String date){
        return flightService.getWeightSummery(number, date);
    }

    @GetMapping("/airport/{IATA}")
    public ResponseEntity<AirportSummary> getAirportSummary(@PathVariable String IATA, @RequestParam("date") String date){
        return flightService.getAirportSummary(IATA, date);
    }
}
