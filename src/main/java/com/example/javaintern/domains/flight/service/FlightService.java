package com.example.javaintern.domains.flight.service;

import com.example.javaintern.domains.cargo.utils.CargoUtil;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.controller.util.AirportSummary;
import com.example.javaintern.domains.flight.controller.util.WeightSummary;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.domains.flight.service.util.OffsetDateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    @Autowired
    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public ResponseEntity<String> saveFlights(List<Flight> flights){
        if(flights.isEmpty())
            throw new NoSuchElementException("There is no flights to add");

        List<Flight> validatedFlights = flights.stream()
                .filter(element -> !element.getArrivalAirportIATACode().equals(element.getDepartureAirportIATACode()))
                        .collect(Collectors.toList());

        flightRepository.saveAll(validatedFlights);

        return new ResponseEntity<>("Flights have been successfully saved", HttpStatus.OK);
    }

    public ResponseEntity<WeightSummary> getWeightSummery(int requestedFlightId, String date){

        Flight requestedFlight = flightRepository.findByFlightNumber(requestedFlightId).orElseThrow(
                () -> new NoSuchElementException("Flight with id: " + requestedFlightId + " hasn't been found")
        );

        OffsetDateTime requestedDate = OffsetDateTimeFormatter.parse(date);
        if(OffsetDateTimeFormatter.alterTimezone(requestedFlight.getDepartureDate()).compareTo(requestedDate) != 0){
            throw new NoSuchElementException("Provided date doesn't match with desirable flight.");
        }

        double baggageWeight = requestedFlight.getBaggages().stream()
                .map(element -> element.getWeightUnit().equals("kg") ? element.getWeight() : element.getWeight()/2.2046)
                .reduce(0.0, Double::sum);

        double airplaneCargoWeight = requestedFlight.getAirplaneCargos().stream()
                .map(element -> element.getWeightUnit().equals("kg") ? element.getWeight() : element.getWeight()/2.2046)
                .reduce(0.0, Double::sum);

        return new ResponseEntity<>(
                new WeightSummary(baggageWeight, airplaneCargoWeight, baggageWeight + airplaneCargoWeight), HttpStatus.OK);
    }

    public ResponseEntity<AirportSummary> getAirportSummary(String IATA, String date){

        Set<Flight> flights = flightRepository.findFlightByDepartureAirportIATACodeOrArrivalAirportIATACode(IATA, IATA)
                                                .orElseThrow(
                                                        () ->  new NoSuchElementException("There aren't any flights with IATA: " + IATA)
                                                );

        LocalDate requestedDate = LocalDate.parse(date);

        Set<Flight> flightsOnAGivenDate = flights.stream().filter(element ->
            element.getDepartureDate().getYear() == requestedDate.getYear() &&
                    element.getDepartureDate().getMonthValue() == requestedDate.getMonthValue() &&
                            element.getDepartureDate().getDayOfMonth() == requestedDate.getDayOfMonth()
        ).collect(Collectors.toSet());

        Set<Flight> departingFlights = flightsOnAGivenDate.stream()
                .filter(element -> element.getDepartureAirportIATACode().equals(IATA))
                .collect(Collectors.toSet());

        Set<Flight> arrivingFlights = flightsOnAGivenDate.stream()
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
