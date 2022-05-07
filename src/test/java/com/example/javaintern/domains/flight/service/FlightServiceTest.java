package com.example.javaintern.domains.flight.service;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.controller.util.AirportSummary;
import com.example.javaintern.domains.flight.controller.util.WeightSummary;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.domains.flight.service.util.OffsetDateTimeFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    public void shouldReturnNoSuchElementExceptionOfEmptyFlightList(){

        String expectedMessage = "There is no flights to add";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            flightService.saveFlights(List.of());
        });

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void shouldSaveAllFlights(){

        String expectedMessage = "Flights have been successfully saved";

        Flight flight1 = new Flight(12, "KRK",
                "GD", OffsetDateTimeFormatter.parse("2017-12-25T07:32:30-01:00"));
        Flight flight2 = new Flight(13, "GD",
                "KRK", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30-01:00"));

        ResponseEntity<String> response = flightService.saveFlights(List.of(flight1, flight2));

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertEquals(response.getBody(), expectedMessage);
    }

    @Test
    public void shouldThrowNoSuchElementExceptionOfMissingFlight(){

        String expectedMessage = "Flight with id: 13 hasn't been found";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            flightService.getWeightSummery(13, "2018-12-25T07:32:30-01:00");
        });

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2018-12-25T07:32:30-01:00",
            "2018-12-25T07:32:30Z",
            "2018-12-2507:32:30-01:00",
            "2018:12:25T07:32:30-01:00",
            "2018/12/25T07:32:30-01:00",})
    public void shouldThrowNoSuchElementExceptionOfDateIncompatibility(String providedDate){

        Flight flight = new Flight(13, "KRK",
                "GD", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00"));

        String expectedMessageNSE = "Provided date doesn't match with desirable flight.";
        String expectedMessageDTE = "Date format is invalid";
        Mockito.when(flightRepository.findByFlightNumber(13)).thenReturn(Optional.of(flight));


        Exception exception = assertThrows(Exception.class, () -> {
            flightService.getWeightSummery(13, providedDate);
        });

        assertTrue(exception.getMessage().equals(expectedMessageNSE) ||
                exception.getMessage().equals(expectedMessageDTE));
    }

    @Test
    public void shouldReturnWeightSummary(){
        Flight flight = new Flight(13, "KRK",
                "GD", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00"));

        Set<Baggage> baggage = Set.of(new Baggage(837, "kg", 293), new Baggage(521, "kg", 185));
        Set<AirplaneCargo> airplaneCargos = Set.of(new AirplaneCargo(430, "kg", 313), new AirplaneCargo(953, "kg", 743));

        flight.setBaggages(baggage);
        flight.setAirplaneCargos(airplaneCargos);

        Mockito.when(flightRepository.findByFlightNumber(13)).thenReturn(Optional.of(flight));

        ResponseEntity<WeightSummary> response = flightService.getWeightSummery(13, "2018-12-25T07:32:30+01:00");

        Assertions.assertEquals(response.getBody().getBaggageWeightKG(), 1358);
        Assertions.assertEquals(response.getBody().getCargoWeightKG(), 1383);
        Assertions.assertEquals(response.getBody().getTotalWeightKG(), 2741);
    }

    @Test
    public void shouldThrowNoSuchElementExceptionOfInvalidIATACode(){

        String expectedMessage = "There aren't any flights with IATA: CSQ";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            flightService.getAirportSummary("CSQ", "2018-12-25");
        });

        Assertions.assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void shouldReturnAirportSummary(){

        Set<Flight> flights = Set.of(
                new Flight(12, "KRK",
                        "GD", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00")),
                new Flight(13, "SDA",
                        "KRK", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00")),
                new Flight(14, "KRK",
                        "ZSF", OffsetDateTimeFormatter.parse("2018-12-24T07:32:30+01:00")),
                new Flight(15, "ASD",
                        "ZSF", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00"))
        );

        Set<Baggage> baggage = Set.of(new Baggage(837, "kg", 293), new Baggage(521, "kg", 185));
        Set<AirplaneCargo> airplaneCargos = Set.of(new AirplaneCargo(430, "kg", 313), new AirplaneCargo(953, "kg", 743));

        flights = flights.stream().peek((element) -> {
            element.setBaggages(baggage);
            element.setAirplaneCargos(airplaneCargos);
        }).collect(Collectors.toSet());

        Mockito.when(flightRepository
                .findFlightByDepartureAirportIATACodeOrArrivalAirportIATACode("KRK", "KRK"))
                .thenReturn(Optional.of(flights));

        ResponseEntity<AirportSummary> response = flightService.getAirportSummary("KRK", "2018-12-25");

        Assertions.assertEquals(response.getBody().getArrivingFlights(), 1);
        Assertions.assertEquals(response.getBody().getDepartingFlights(), 1);
        Assertions.assertEquals(478, response.getBody().getQuantityOfBaggageArriving());
        Assertions.assertEquals(478, response.getBody().getQuantityOfBaggageDeparting());
    }
}
