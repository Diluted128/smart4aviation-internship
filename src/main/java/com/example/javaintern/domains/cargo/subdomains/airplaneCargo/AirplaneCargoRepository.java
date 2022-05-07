package com.example.javaintern.domains.cargo.subdomains.airplaneCargo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneCargoRepository extends JpaRepository<AirplaneCargo, Integer> {
}
