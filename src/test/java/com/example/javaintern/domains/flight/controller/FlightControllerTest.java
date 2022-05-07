package com.example.javaintern.domains.flight.controller;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.controller.util.AirportSummary;
import com.example.javaintern.domains.flight.controller.util.WeightSummary;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.domains.flight.service.util.OffsetDateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class FlightControllerTest {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Gson gson = new Gson();

    @Test
    public void shouldSaveAllFlights() throws Exception {

        Map<String, String> flight1 =  new HashMap<>();
        flight1.put("flightNumber", "35");
        flight1.put("departureAirportIATACode", "GDA");
        flight1.put("arrivalAirportIATACode", "KRK");
        flight1.put("departureDate", "2018-12-25T07:32:30+01:00");

        Map<String, String> flight2 =  new HashMap<>();
        flight2.put("flightNumber", "12");
        flight2.put("departureAirportIATACode", "WNF");
        flight2.put("arrivalAirportIATACode", "KRK");
        flight2.put("departureDate", "2018-12-25T07:32:30+01:00");

        String json = objectMapper.writeValueAsString(List.of(flight1, flight2));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/flight")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        Assertions.assertEquals("Flights have been successfully saved", mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void shouldReturnWeightSummary() throws Exception {

        saveTestData();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/flight/14/weight")
                        .param("date","2018-12-24T07:32:30+01:00")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                     .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        WeightSummary weightSummary = gson.fromJson(responseBody, WeightSummary.class);

        Assertions.assertEquals(1358, weightSummary.getBaggageWeightKG());
        Assertions.assertEquals(1383, weightSummary.getCargoWeightKG());
        Assertions.assertEquals(2741, weightSummary.getTotalWeightKG());
    }

    @Test
    public void shouldReturnAirportSummary() throws Exception {

        saveTestData();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/airport/KRK")
                        .param("date","2018-12-25")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();

        AirportSummary airportSummary = gson.fromJson(responseBody, AirportSummary.class);

        Assertions.assertEquals(1, airportSummary.getArrivingFlights());
        Assertions.assertEquals(1, airportSummary.getDepartingFlights());
        Assertions.assertEquals(478, airportSummary.getQuantityOfBaggageArriving());
        Assertions.assertEquals(478, airportSummary.getQuantityOfBaggageDeparting());
    }

    public void saveTestData(){
        List<Flight> flights = List.of(
                new Flight(12, "KRK",
                        "GDA", OffsetDateTimeFormatter.parse("2018-12-25T07:32:30+01:00")),
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
        }).collect(Collectors.toList());

        flightRepository.saveAll(flights);
    }
}
