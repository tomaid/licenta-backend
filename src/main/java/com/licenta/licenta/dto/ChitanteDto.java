package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class ChitanteDto {
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String serieChitanta;
    @Getter @Setter
    private String detalii;
    @Getter @Setter
    private Date dataAchitare;
    @Getter @Setter
    private double suma;
}
