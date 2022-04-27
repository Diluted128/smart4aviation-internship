package com.example.javaintern.domains.cargo.controller;

import com.example.javaintern.domains.cargo.Cargo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class CargoController {

    @PostMapping("/cargo")
    public void saveFlightDetails(@RequestBody Set<Cargo> cargos) {
          cargos.forEach(System.out::println);
    }
}
