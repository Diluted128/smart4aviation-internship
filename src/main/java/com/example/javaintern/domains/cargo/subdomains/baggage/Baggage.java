package com.example.javaintern.domains.cargo.subdomains.baggage;

import com.example.javaintern.domains.cargo.utils.CargoUtil;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class Baggage extends CargoUtil {
    public Baggage(int weight, String weightUnit, int pieces){
        super(weight, weightUnit, pieces);
    }
}
