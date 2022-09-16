package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class AlgoGeneticProduseDto {
    @Getter @Setter
    private Integer idCategorie;
    @Getter @Setter
    private Integer cicluNumere;
    @Getter @Setter
    private Integer durataCiclu;
}
