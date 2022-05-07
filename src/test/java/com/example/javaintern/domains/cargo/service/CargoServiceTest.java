package com.example.javaintern.domains.cargo.service;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.cargo.subdomains.baggage.Baggage;
import com.example.javaintern.domains.cargo.utils.TotalCargo;
import com.example.javaintern.domains.flight.Flight;
import com.example.javaintern.domains.flight.repository.FlightRepository;
import com.example.javaintern.domains.flight.service.util.OffsetDateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private CargoService cargoService;

    @Test
    public void shouldAssignCargosToFlight() {

        Flight flight = new Flight(28, "KRK", "GDK", OffsetDateTimeFormatter.parse("2017-12-25T07:32:30-01:00"));

        Set<Baggage> baggage = Set.of(new Baggage(837, "kg", 293));
        Set<AirplaneCargo> airplaneCargos = Set.of(new AirplaneCargo(430, "kg", 313));
        TotalCargo totalCargo = new TotalCargo(28, baggage, airplaneCargos);


        Mockito.when(flightRepository.findById(totalCargo.getFlightId())).thenReturn(Optional.of(flight));

        String expectedResponseBody = "All cargos has been assigned to their flights";

        ResponseEntity<String> response = cargoService.saveALlCargos(List.of(totalCargo));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(response.getBody(), expectedResponseBody);
    }

    @Test
    public void shouldThrowNoSuchElementExceptionOfEmptyFlightList() {

        List<TotalCargo> totalCargoList = List.of();

        String expectedMessage = "Provided list is empty.";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cargoService.saveALlCargos(totalCargoList);
        });

        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void shouldReturnNoSuchElementExceptionOfNonExistentFlightID() {
        TotalCargo totalCargo = new TotalCargo(
                32312,
                Set.of(),
                Set.of()
        );

        String expectedMessage = "Flight with id: " + totalCargo.getFlightId() + " hasn't been found";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            cargoService.saveALlCargos(List.of(totalCargo));
        });

        assertEquals(exception.getMessage(), expectedMessage);
    }
}
