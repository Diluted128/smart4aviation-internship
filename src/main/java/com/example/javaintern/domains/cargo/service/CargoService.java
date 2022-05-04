package com.example.javaintern.domains.cargo.service;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.utils.TotalCargo;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CargoService {

    private final FlightRepository flightRepository;

    @Autowired
    public CargoService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public ResponseEntity<String> saveALlCargos(List<TotalCargo> totalCargoSet){
        if(totalCargoSet.isEmpty())
            throw new NoSuchElementException("Provided list is empty.");
        else{
            Flight flight;

            for(TotalCargo totalCargo : totalCargoSet) {

                flight = flightRepository.findById(totalCargo.getFlightId()).orElseThrow(
                        () -> new NoSuchElementException("Flight with id: " + totalCargo.getFlightId() + " hasn't been found")
                );

                Set<Baggage> baggage = new HashSet<>();
                Set<AirplaneCargo> airplaneCargos = new HashSet<>();

                for(Baggage singleBaggage : totalCargo.getBaggages()){
                    singleBaggage.setFlight(flight);
                    baggage.add(singleBaggage);
                }

                for(AirplaneCargo airplaneCargo : totalCargo.getAirplaneCargos()){
                    airplaneCargo.setFlight(flight);
                    airplaneCargos.add(airplaneCargo);
                }

                flight.setBaggages(baggage);
                flight.setAirplaneCargos(airplaneCargos);
                flightRepository.save(flight);
            }

            return new ResponseEntity<>("All cargos has been assigned to their flights", HttpStatus.OK);
        }
    }
}

