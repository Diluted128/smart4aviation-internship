package com.example.javaintern.domains.cargo;

import com.example.javaintern.domains.cargo.utils.Baggage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class Cargo {
    private int flightId;
    private Set<Baggage> baggage;
    private Set<Cargo> cargo;
}
