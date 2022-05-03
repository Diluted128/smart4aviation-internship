package com.example.javaintern.domains.flight.repository;

import com.example.javaintern.domains.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

    Optional<Flight> findByFlightNumber(int number);

    Optional<Set<Flight>> findFlightByDepartureAirportIATACodeOrArrivalAirportIATACode(String DepartureAirportIATACode, String ArrivalAirportIATACode);
}
