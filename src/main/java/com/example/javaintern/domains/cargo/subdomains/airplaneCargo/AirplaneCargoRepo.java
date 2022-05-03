package com.example.javaintern.domains.cargo.subdomains.airplaneCargo;

import com.example.javaintern.domains.cargo.subdomains.airplaneCargo.AirplaneCargo;
import com.example.javaintern.domains.flight.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AirplaneCargoRepo extends JpaRepository<AirplaneCargo, Integer> {

    Set<AirplaneCargo> getAirplaneCargoByFlight(Flight flight);
}
