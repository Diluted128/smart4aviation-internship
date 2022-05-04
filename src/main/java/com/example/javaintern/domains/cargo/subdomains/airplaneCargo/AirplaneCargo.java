package com.example.javaintern.domains.cargo.subdomains.airplaneCargo;

import com.example.javaintern.domains.cargo.utils.CargoUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class AirplaneCargo extends CargoUtil {
    public AirplaneCargo(int weight, String weightUnit, int pieces){
        super(weight, weightUnit, pieces);
    }
}
