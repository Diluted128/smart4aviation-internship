package com.example.javaintern.domains.flight.service;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargoRepo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.subdomains.baggage.BaggageRepo;
import com.example.javaintern.domains.cargo.utils.CargoUtil;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.controller.util.AirportSummary;
import com.example.javaintern.domains.flight.controller.util.WeightSummary;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.exception.exceptions.FlightNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;
    private final BaggageRepo baggageRepo;
    private final AirplaneCargoRepo airplaneCargoRepo;

    @Autowired
    public FlightService(FlightRepository flightRepository, BaggageRepo baggageRepo, AirplaneCargoRepo airplaneCargoRepo) {
        this.flightRepository = flightRepository;
        this.baggageRepo = baggageRepo;
        this.airplaneCargoRepo = airplaneCargoRepo;
    }

    public ResponseEntity<String> saveFlights(List<Flight> flights){
        if(flights.isEmpty())
            throw new FlightNotFoundException("There is no flights to add");

        List<Flight> validatedFlights = flights.stream()
                .filter(element -> !element.getArrivalAirportIATACode().equals(element.getDepartureAirportIATACode()))
                        .collect(Collectors.toList());

        flightRepository.saveAll(validatedFlights);

        return new ResponseEntity<>("Flights have been successfully saved", HttpStatus.OK);
    }

    public ResponseEntity<WeightSummary> getWeightSummery(int number, String date){

        Flight requestedFlight = flightRepository.findByFlightNumber(number).orElseThrow(
                () -> new RuntimeException("niepoprawne id")
        );

        OffsetDateTime offsetDateTime1 = OffsetDateTime.parse(date);
        OffsetDateTime nowInUTC1 = offsetDateTime1 .withOffsetSameInstant(ZoneOffset.of( "+00:00" ));
        OffsetDateTime nowInUTC2 = requestedFlight.getDepartureDate().withOffsetSameInstant(ZoneOffset.of( "+00:00" ));
        if(nowInUTC1.compareTo(nowInUTC2) != 0){
            throw new RuntimeException("zla data");
        }

        Set<Baggage> baggages = baggageRepo.getBaggageByFlight(requestedFlight);
        Set<AirplaneCargo> airplaneCargos = airplaneCargoRepo.getAirplaneCargoByFlight(requestedFlight);

        int baggageWeight = baggages.stream().map(CargoUtil::getWeight).reduce(0, Integer::sum);
        int airplaneCargoWeight = airplaneCargos.stream().map(CargoUtil::getWeight).reduce(0, Integer::sum);

        return new ResponseEntity<>(
                new WeightSummary(baggageWeight, airplaneCargoWeight, baggageWeight + airplaneCargoWeight), HttpStatus.OK);
    }

    public ResponseEntity<AirportSummary> getAirportSummary(String IATA, String date){

        Set<Flight> flights = flightRepository.findFlightByDepartureAirportIATACodeOrArrivalAirportIATACode(IATA, IATA)
                                                .orElseThrow(
                                                        () ->  new RuntimeException("nie znaleziono samolotow")
                                                );
        OffsetDateTime offsetDateTime1 = OffsetDateTime.parse(date);
        OffsetDateTime nowInUTC = offsetDateTime1 .withOffsetSameInstant(ZoneOffset.of( "+00:00" ));

        Set<Flight> flights1 = flights.stream().filter(element ->
            element.getDepartureDate().getYear() == nowInUTC.getYear() &&
                    element.getDepartureDate().getMonthValue() == nowInUTC.getMonthValue() &&
                            element.getDepartureDate().getDayOfMonth() == nowInUTC.getDayOfMonth()
        ).collect(Collectors.toSet());

        Set<Flight> departingFlights = flights1.stream()
                .filter(element -> element.getDepartureAirportIATACode().equals(IATA))
                .collect(Collectors.toSet());

        Set<Flight> arrivingFlights = flights1.stream()
                .filter(element -> element.getArrivalAirportIATACode().equals(IATA))
                .collect(Collectors.toSet());

        int quantityOfDepartureFlights = departingFlights.stream()
                .flatMap(element -> element.getBaggages().stream())
                .map(CargoUtil::getPieces)
                .reduce(0 , Integer::sum);

        int quantityOfArriveFlights = arrivingFlights.stream().flatMap(element -> element.getBaggages().stream())
                .map(CargoUtil::getPieces)
                .reduce(0 , Integer::sum);

        return new ResponseEntity<>(
                new AirportSummary(departingFlights.size(), arrivingFlights.size(), quantityOfDepartureFlights, quantityOfArriveFlights), HttpStatus.OK);
    }
}
