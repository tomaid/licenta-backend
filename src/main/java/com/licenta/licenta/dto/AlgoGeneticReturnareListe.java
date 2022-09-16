package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public class AlgoGeneticReturnareListe {
    @Getter @Setter
    private List<AlgoGeneticReturnareLista> produsePrimite;
    @Getter @Setter
    private Integer generatii;
    @Getter @Setter
    private double pretTotal;
    @Getter @Setter
    private double totalKw;
    @Getter @Setter
    private String varianta;


}
