package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class AlgoGeneticProduseArrayDto {
    @Setter @Getter
    private List<AlgoGeneticProduseDto> produs;
    @Setter @Getter
    private double pret;

}
