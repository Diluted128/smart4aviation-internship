package com.example.javaintern.domains.cargo.service;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargoRepo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.subdomains.baggage.BaggageRepo;
import com.example.javaintern.domains.cargo.utils.TotalCargo;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.exception.exceptions.FlightNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CargoService {

    private final BaggageRepo baggageRepo;
    private final AirplaneCargoRepo airplaneCargoRepo;
    private final FlightRepository flightRepository;

    @Autowired
    public CargoService(BaggageRepo baggageRepo, AirplaneCargoRepo airplaneCargoRepo, FlightRepository flightRepository) {
        this.baggageRepo = baggageRepo;
        this.airplaneCargoRepo = airplaneCargoRepo;
        this.flightRepository = flightRepository;
    }

    public ResponseEntity<String> saveALlCargos(List<TotalCargo> totalCargoSet){
        if(totalCargoSet.isEmpty())
            throw new FlightNotFoundException("Not found any cargos");
        else{
            Flight flight;

            for(TotalCargo totalCargo : totalCargoSet) {

                flight = flightRepository.findById(totalCargo.getFlightId()).orElseThrow(
                        () -> new FlightNotFoundException("Flight not found")
                );

                Set<Baggage> baggages = new HashSet<>();
                Set<AirplaneCargo> airplaneCargos = new HashSet<>();

                for(Baggage baggage : totalCargo.getBaggages()){
                    baggage.setFlight(flight);
                    baggages.add(baggage);
                }

                for(AirplaneCargo airplaneCargo : totalCargo.getAirplaneCargos()){
                    airplaneCargo.setFlight(flight);
                    airplaneCargos.add(airplaneCargo);
                }

                flight.setBaggages(baggages);
                flight.setAirplaneCargos(airplaneCargos);
                flightRepository.save(flight);
            }

            return new ResponseEntity<>("All cargos has been assigned to their flights", HttpStatus.OK);
        }
    }
}
