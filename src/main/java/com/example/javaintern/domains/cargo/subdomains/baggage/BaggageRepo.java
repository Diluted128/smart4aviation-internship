package com.example.javaintern.domains.cargo.subdomains.baggage;

import com.example.javaintern.domains.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BaggageRepo extends JpaRepository<Baggage, Integer> {

    Set<Baggage> getBaggageByFlight(Flight flight);
}
