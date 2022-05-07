package com.example.javaintern.domains.cargo.controller;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.utils.TotalCargo;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.domains.flight.service.util.OffsetDateTimeFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Set;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    public void shouldAssignCargoToFlight() throws Exception {
        Flight flight = new Flight(28, "KRK", "GDK", OffsetDateTimeFormatter.parse("2017-12-25T07:32:30-01:00"));

        Set<Baggage> baggage = Set.of(new Baggage(837, "kg", 293));
        Set<AirplaneCargo> airplaneCargos = Set.of(new AirplaneCargo(430, "kg", 313));
        TotalCargo totalCargo = new TotalCargo(1, baggage, airplaneCargos);

        flightRepository.save(flight);

        ObjectMapper objectMapper = new ObjectMapper();
        String totalCargoJSON = objectMapper.writeValueAsString(List.of(totalCargo));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/cargo")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(totalCargoJSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        String expectedMessage = "All cargos has been assigned to their flights";

        Assertions.assertEquals(mvcResult.getResponse().getContentAsString(), expectedMessage);
    }
}
