package com.example.javaintern.domains.cargo.controller;

import com.example.javaintern.domains.cargo.service.CargoService;
import com.example.javaintern.domains.cargo.utils.TotalCargo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class CargoController {

    private final CargoService cargoService;

    @Autowired
    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PostMapping("/cargo")
    public ResponseEntity<String> saveFlightDetails(@RequestBody List<TotalCargo> cargos) {
        return cargoService.saveALlCargos(cargos);
    }
}
