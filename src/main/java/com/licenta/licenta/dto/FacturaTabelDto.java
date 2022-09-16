package com.licenta.licenta.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
public class FacturaTabelDto {
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String apartamentNume;
    @Getter @Setter
    private double valoare;
    @Getter @Setter
    private double valoareRestante;
    @Getter @Setter
    private Date data;
    @Getter @Setter
    private Date dataScadenta;
    @Getter @Setter
    private String status;

}
